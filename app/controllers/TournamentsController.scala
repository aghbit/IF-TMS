package controllers

import java.util

import com.mongodb.{DBObject, BasicDBObject}
import com.mongodb.casbah.commons.MongoDBObject
import controllers.UsersController._
import controllers.security.{TournamentAction, TokenImpl, AuthorizationAction}
import models.enums.ListEnum
import models.player.players.Captain
import models.strategy.{Match, EliminationTree}
import models.strategy.eliminationtrees.DoubleEliminationTree
import models.strategy.strategies.{DoubleEliminationStrategy, SingleEliminationStrategy}
import models.team.Team
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamentstates.BeforeEnrollment
import models.tournament.tournamentfields.{TournamentDescription, TournamentStaff, TournamentProperties}
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import play.api.libs.json._
import org.bson.types.ObjectId
import play.api.mvc.{Action, Controller}
import repositories.{TeamRepository, EliminationTreeRepository, TournamentRepository}
import models.tournament.tournamentfields.JsonFormatTournamentProperties._
import scala.collection.JavaConversions._




import scala.concurrent.Future

/**
 * Created by Szymek.
 */
object TournamentsController extends Controller{

  private val repository = new TournamentRepository()
  private val eliminationTreeRepository = new EliminationTreeRepository()


  def createTournament() = AuthorizationAction.async(parse.json) {
    request =>
      val tournamentProperties = request.body.validate[TournamentProperties].asEither
      tournamentProperties match {
        case Right(properties) =>
          val userID = TokenImpl(request.headers.get("token").get).getUserID
          val tournamentStaff =  new TournamentStaff(userID, new util.ArrayList())
          val beforeEnrollment = BeforeEnrollment(properties, tournamentStaff, DoubleEliminationStrategy)
          try {
            repository.insert(beforeEnrollment)
            Future.successful(Created)
          } catch {
            case e:IllegalArgumentException => Future.successful(UnprocessableEntity("Tournament can't be saved!"))
            case e:Throwable => Future.failed(e)
          }
        case Left(e) => Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(e)))
      }
  }
  def nextEnrollmentState() = AuthorizationAction.async(parse.json) {
    request =>
      val tournamentID = request.body.\("_id").validate[String].asEither
      tournamentID match {
        case Right(id) =>
          val query = new BasicDBObject("_id", new ObjectId(id))
          val tournament = repository.find(query)
          val enrollmentStateTournament = tournament.get(ListEnum.head).startNext()
          try {
            repository.insert(enrollmentStateTournament)
            Future.successful(Created)
          } catch {
            case e: IllegalArgumentException => Future.successful(UnprocessableEntity("Error starting enrollment!"))
            case e: Throwable => Future.failed(e)
          }
        case Left(e) => Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(e)))
      }
  }
  def getMyTournaments = AuthorizationAction.async {
    request =>
      val userID = TokenImpl(request.headers.get("token").get).getUserID
      val query = new BasicDBObject("staff.admin", userID)
      val tournaments = repository.find(query)
      val tournamentsJson = tournaments.map(tournament => tournament.toJson)
      Future.successful(Ok(Json.toJson(tournamentsJson)))
  }
  def getTournaments = Action.async {
    request =>
      val query = new BasicDBObject()
      val tournaments = repository.find(query)
      val tournamentsJson = tournaments.map(tournament => tournament.toJson)
      Future.successful(Ok(Json.toJson(tournamentsJson)))
  }

  def getTournament(id: String) = TournamentAction(id).async {
    request =>
      val tournamentJson = request.tournament.toJson
      Future.successful(Ok(tournamentJson));
  }

  def getTournamentTree(id: String) = TournamentAction(id).async{
    request =>
      val query = MongoDBObject("_id" -> request.tournament._id)
      val eliminationTreeRepository = new EliminationTreeRepository()
      eliminationTreeRepository.findOne(query) match {
        case Some(t) => Future.successful(Ok(t.toJson()))
        case None => Future.successful(NotFound("Tournament with this id hasn't any tree."))
      }

  }

  def generateTournamentTree(id: String) = TournamentAction(id).async(parse.json) {
    request =>
        val tournamentId = request.tournament._id

        if(!eliminationTreeRepository.contains(new BasicDBObject("_id", tournamentId))){

          val query = new BasicDBObject("_id", tournamentId)
          val tournament = repository.find(query).get(ListEnum.head)

          try {
            val tree = tournament.generateTree()
            eliminationTreeRepository.insert(tree)
            Future.successful(Created("Tournament tree has been successfully created."))
          } catch {
            case e:IllegalArgumentException => Future.successful(UnprocessableEntity(e.getMessage))
            case e:Exception => Future.failed(e)
          }
        }else{
          Future.successful(MethodNotAllowed("This tournament has elimination tree. Can't generate."))
        }
  }


  def removeTournamentTree(id: String) = TournamentAction(id).async(parse.json) {
    request =>
        val tournamentId = request.tournament._id
        if(eliminationTreeRepository.contains(new BasicDBObject("_id", tournamentId))){
          eliminationTreeRepository.remove(new BasicDBObject("_id", tournamentId))
          Future.successful(Ok("Tournament tree was removed!"))
        }else{
          Future.successful(NotFound("Tournament id is invalid!"))
        }

  }

  def updateMatch(id: String, matchId: Int) = TournamentAction(id).async(parse.json) {
    request =>
      //val tournamentType = request.tournament.tournamentType
      val tournamentType = BeachVolleyball
      val tournamentId = request.tournament._id
      eliminationTreeRepository.findOne(new BasicDBObject("_id", tournamentId)) match {
        case Some(tree) =>
          //only for tests
          val host = new BeachVolleyballTeam(new ObjectId(request.body.\("host").\("_id").validate[String].get),
            request.body.\("host").\("name").validate[String].get, 2, 0)
          val guest = new BeachVolleyballTeam(new ObjectId(request.body.\("guest").\("_id").validate[String].get),
            request.body.\("guest").\("name").validate[String].get, 2, 0)
          val matchUpdated = new Match(matchId, Some(host), Some(guest), tournamentType.getNewScore())

          matchUpdated.score = tournamentType.getNewScore()
          val sets = request.body.\("sets").validate[List[JsObject]].get
          sets.foreach( set => {
            val hostScore = set.\("host").validate[Int].get
            val guestScore = set.\("guest").validate[Int].get
            matchUpdated.score.addSet()
            matchUpdated.score.setScoreInLastSet(hostScore, guestScore)
          })
          DoubleEliminationStrategy.updateMatchResult(tree, matchUpdated)
          eliminationTreeRepository.remove(new BasicDBObject("_id", tournamentId))
          eliminationTreeRepository.insert(tree)
          Future.successful(Ok(""))

        case None => Future.successful(NotFound("This tournament has elimination tree. Can't update match"))

      }
  }
}

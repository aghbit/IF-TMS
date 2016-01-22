package controllers

import java.util

import com.mongodb.BasicDBObject
import com.mongodb.casbah.commons.MongoDBObject
import controllers.security.{AuthorizationAction, TokenImpl, TournamentAction}
import models.enums.ListEnum
import models.player.players.{Captain, DefaultPlayerImpl}
import models.strategy.strategies.{DoubleEliminationStrategy, RoundRobinStrategy, SingleEliminationStrategy}
import models.strategy.structures.{EliminationTable, EliminationTree}
import models.strategy.{EliminationStrategy, Match}
import models.team.Team
import models.tournament.tournamentfields.JsonFormatTournamentProperties._
import models.tournament.tournamentfields._
import models.tournament.tournamentstates.BeforeEnrollment
import models.tournament.tournamenttype.TournamentType
import models.tournament.tournamenttype.tournamenttypes.{BeachVolleyball, Speedminton, Volleyball}
import org.bson.types.ObjectId
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import repositories._

import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.util.Random

/**
 * Created by Szymek.
 */
object TournamentsController extends Controller{

  private val repository = new TournamentRepository()
  private val eliminationTreeRepository = new EliminationTreeRepository()
  private val eliminationTableRepository = new EliminationTableRepository()


  def createTournament() = AuthorizationAction.async(parse.json) {
    request =>
      val tournamentProperties = request.body.\("properties").validate[TournamentProperties].asEither
      val tournamentStrategy = request.body.\("strategy").validate[String].asEither
      val tournamentDiscipline = request.body.\("discipline").validate[String].asEither

      val strategy = tournamentStrategy match {
        case Right("SingleEliminationStrategy") => Some(SingleEliminationStrategy)
        case Right("DoubleEliminationStrategy") => Some(DoubleEliminationStrategy)
        case Right("RoundRobinStrategy") => Some(RoundRobinStrategy)
        case _ => None
      }
      val discipline = tournamentDiscipline match {
        case Right("BeachVolleyball") => Some(BeachVolleyball)
        case Right("Volleyball") => Some(Volleyball)
        case Right("Speedminton") => Some(Speedminton)
        case _ => None

      }
      (tournamentProperties, strategy, discipline) match {
        case (Right(properties), Some(eliminationStrategy), Some(d)) =>
          val userID = TokenImpl(request.headers.get("token").get).getUserID
          val tournamentStaff =  new TournamentStaff(userID, new util.ArrayList())
          val beforeEnrollment = BeforeEnrollment(properties, tournamentStaff, eliminationStrategy, d)
          try {
            repository.insert(beforeEnrollment)
            Future.successful(Created)
          } catch {
            case e:IllegalArgumentException => Future.successful(UnprocessableEntity("Tournament can't be saved!"))
            case e:Throwable => Future.failed(e)
          }
        case (Left(e), _, _) => Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(e)))
        case (_, None, _) => Future.successful(BadRequest("Detected error: This strategy isn't available."))
        case (_, _, None) => Future.successful(BadRequest("Detected error: This discipline isn't available."))
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

  def getTournament(id: String) = Action.async {
    request =>
      repository.findOne(MongoDBObject("_id" -> new ObjectId(id))) match {
        case Some(tournament) => Future.successful(Ok(tournament.toJson));
        case None => Future.successful(NotFound("Tournament not found!"))
      }
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
            tree match {
              case t:EliminationTree => eliminationTreeRepository.insert(t)
              case t:EliminationTable => ???
            }
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
      println(request.body.toString())
      val discipline = request.tournament.discipline
      val tournamentId = request.tournament._id
      eliminationTreeRepository.findOne(new BasicDBObject("_id", tournamentId)) match {
        case Some(tree) =>
          //only for tests
          val host = discipline.getNewParticipant(new ObjectId(request.body.\("host").\("_id").validate[String].get),
            request.body.\("host").\("name").validate[String].get)
          val guest = discipline.getNewParticipant(new ObjectId(request.body.\("guest").\("_id").validate[String].get),
            request.body.\("guest").\("name").validate[String].get)
          val matchUpdated = new Match(matchId, Some(host), Some(guest), discipline.getNewScore())

          matchUpdated.score = discipline.getNewScore()
          val score = request.body.\("sets").validate[List[JsObject]].get
          score.foreach( pointsContainerJson => {
            val hostScore = pointsContainerJson.\("host")
            val guestScore = pointsContainerJson.\("guest")
            val typeScore = pointsContainerJson.\("type").validate[String].get.toString
            val pointsContainer = discipline.getNewPointsContainer(typeScore, hostScore, guestScore)
            matchUpdated.score.addPointsContainer(pointsContainer)
          })

          tree.strategy.updateMatchResult(tree, matchUpdated)
          eliminationTreeRepository.remove(new BasicDBObject("_id", tournamentId))
          eliminationTreeRepository.insert(tree)
          Future.successful(Ok(""))

        case None => Future.successful(NotFound("This tournament has elimination tree. Can't update match"))

      }
  }

  def initDB() = Action.async{
    request =>
      initDBHelper("BVTEST8SE", 10, SingleEliminationStrategy, BeachVolleyball, 8, 2)
      initDBHelper("BVTEST16SE", 18, SingleEliminationStrategy, BeachVolleyball, 16, 2)
      initDBHelper("BVTEST32SE", 34, SingleEliminationStrategy, BeachVolleyball, 32, 2)

      initDBHelper("BVTEST8DE", 10, DoubleEliminationStrategy, BeachVolleyball, 8, 2)
      initDBHelper("BVTEST16DE", 18, DoubleEliminationStrategy, BeachVolleyball, 16, 2)
      initDBHelper("BVTEST32DE", 34, DoubleEliminationStrategy, BeachVolleyball, 32, 2)

      initDBHelper("VTEST8SE", 10, SingleEliminationStrategy, Volleyball, 8, 6)
      initDBHelper("VTEST16SE", 18, SingleEliminationStrategy, Volleyball, 16, 6)
      initDBHelper("VTEST32SE", 34, SingleEliminationStrategy, Volleyball, 32, 6)

      initDBHelper("VTEST8DE", 10, DoubleEliminationStrategy, Volleyball, 8, 6)
      initDBHelper("VTEST16DE", 18, DoubleEliminationStrategy, Volleyball, 16, 6)
      initDBHelper("VTEST32DE", 34, DoubleEliminationStrategy, Volleyball, 32, 6)
      //initDBHelper("VTEST128DE", 130, DoubleEliminationStrategy, Volleyball, 128, 6)

      Future.successful(Ok("OK"))
  }

  private def initDBHelper(name:String, maxTeamsNumber:Int, eliminationStrategy: EliminationStrategy,
                            discipline:TournamentType, teamsNumber:Int, playersInTeamNumber:Int) = {
    val names = List("Noah",	"Emma", "Liam",	"Olivia",	"Mason",
      "Sophia",	"Jacob", "Isabell", "William",	"Ava",  "Ethan",	"Mia")
    val surnames = List("SMITH",  "JOHNSON", "WILLIAMS", "JONES", "BROWN",
      "DAVIS", "MILLER", "WILSON")
    val phones = List("784588969", "784555333", "999333222", "543523444")
    val mails = List("pas@cs.pl", "knbit@edu.pl", "lol32@jd.pl", "pio@a.pl")
    val teamNames = (for(i<- 1 to 160) yield "Team"+i).toList

    var tournament = BeforeEnrollment(
      new TournamentProperties(new TournamentDescription(name, "KRK", "description"),
        new TournamentTerm(
          new DateTime().plusDays(2),
          new DateTime().plusDays(10),
          new DateTime().plusDays(12),
          new DateTime().plusDays(14),
          new DateTime().plusDays(16)
        ),
        new TournamentSettings(8, maxTeamsNumber, false, 1)
      ),
      new TournamentStaff(new ObjectId("569431b8b50197f6e366c8ec"), new util.ArrayList()),
            //SZYMEK: 55ca3b9744ae7468a4dca767
            //MACIEK: "569431b8b50197f6e366c8ec"
      eliminationStrategy,
      discipline)
    val playerRepo = new PlayerRepository
    val teamRepo = new TeamRepository
    tournament = tournament.startNext()
    for(i<- 0 until teamsNumber) {
      val team = discipline.getNewParticipant(teamNames(i)).asInstanceOf[Team]
      val captain: Captain = Captain(Random.shuffle(names).head,
        Random.shuffle(surnames).head,
        Random.shuffle(phones).head,
        Random.shuffle(mails).head)
      team.addPlayer(captain)
      team.setCaptain(captain)
      playerRepo.insert(captain)
      for(i <- 1 until playersInTeamNumber){
        val player: DefaultPlayerImpl = DefaultPlayerImpl(Random.shuffle(names).head, Random.shuffle(surnames).head)
        team.addPlayer(player)
        playerRepo.insert(player)
      }
      tournament.addParticipant(team)
      teamRepo.insert(team)
    }
    val repo = new TournamentRepository
    repo.insert(tournament)
  }

  def getTournamentTable(id: String) = TournamentAction(id).async{
    request =>
      val query = MongoDBObject("_id" -> request.tournament._id)
      eliminationTableRepository.findOne(query) match {
        case Some(t) => Future.successful(Ok(t.toJson))
        case None => Future.successful(NotFound("Tournament with this id hasn't any table."))
      }

  }

  def generateTournamentTable(id: String) = TournamentAction(id).async(parse.json) {
    request =>
      val tournamentId = request.tournament._id

      if(!eliminationTableRepository.contains(new BasicDBObject("_id", tournamentId))){

        val query = new BasicDBObject("_id", tournamentId)
        val tournament = repository.find(query).get(ListEnum.head)

        try {
          val tree = tournament.generateTree()
          tree match {
            case t:EliminationTable => eliminationTableRepository.insert(t)
            case t:EliminationTree => ???
          }
          Future.successful(Created("Tournament table has been successfully created."))
        } catch {
          case e:IllegalArgumentException => Future.successful(UnprocessableEntity(e.getMessage))
          case e:Exception => Future.failed(e)
        }
      }else{
        Future.successful(MethodNotAllowed("This tournament has elimination table. Can't generate."))
      }
  }

  def removeTournamentTable(id: String) = TournamentAction(id).async(parse.json) {
    request =>
      val tournamentId = request.tournament._id
      if(eliminationTableRepository.contains(new BasicDBObject("_id", tournamentId))){
        eliminationTableRepository.remove(new BasicDBObject("_id", tournamentId))
        Future.successful(Ok("Tournament table was removed!"))
      }else{
        Future.successful(NotFound("Tournament id is invalid!"))
      }

  }
}

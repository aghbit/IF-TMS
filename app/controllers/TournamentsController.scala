package controllers

import java.util

import controllers.UsersController._
import controllers.security.{TokenImpl, AuthorizationAction}
import models.enums.ListEnum
import models.strategy.EliminationTree
import models.strategy.eliminationtrees.DoubleEliminationTree
import models.strategy.strategies.{DoubleEliminationStrategy, SingleEliminationStrategy}
import models.tournament.tournamentstates.BeforeEnrollment
import models.tournament.tournamentfields.{TournamentDescription, TournamentStaff, TournamentProperties}
import play.api.libs.json.{JsBoolean, JsError, Json}
import org.bson.types.ObjectId
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import repositories.TournamentRepository
import models.tournament.tournamentfields.JsonFormatTournamentProperties._
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import scala.collection.JavaConversions._



import scala.concurrent.Future

/**
 * Created by Szymek.
 */
object TournamentsController extends Controller{

  private val repository = new TournamentRepository()

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
          val query = new Query(Criteria where "_id" is new ObjectId(id))
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
      val query = new Query(Criteria where "staff.admin" is userID)
      val tournaments = repository.find(query)
      val tournamentsJson = tournaments.map(tournament => tournament.toJson)
      Future.successful(Ok(Json.toJson(tournamentsJson)))
  }
  def getTournaments = Action.async {
    request =>
      val query = new Query()
      val tournaments = repository.find(query)
      val tournamentsJson = tournaments.map(tournament => tournament.toJson)
      Future.successful(Ok(Json.toJson(tournamentsJson)))
  }

  def getTournament(id: String) = AuthorizationAction.async {
    request =>
      val query = new Query(Criteria where "_id" is new ObjectId(id))
      val tournament = repository.find(query).get(ListEnum.head)
      val tournamentJson = tournament.toJson
      Future.successful(Ok(tournamentJson));
  }


}

package controllers

import java.util

import controllers.security.{TokenImpl, AuthorizationAction}
import models.enums.ListEnum
import models.strategy.strategies.SingleEliminationStrategy
import models.tournament.tournamentstates.BeforeEnrollment
import models.tournament.tournamentfields.{TournamentDescription, TournamentStaff, TournamentProperties}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
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
          val beforeEnrollment = BeforeEnrollment(properties, new SingleEliminationStrategy(), tournamentStaff)
          val tournament = beforeEnrollment.startNext()
          try {
            repository.insert(tournament)
            Future.successful(Created)
          } catch {
            case e:IllegalArgumentException => Future.successful(UnprocessableEntity("Tournament can't be saved!"))
            case e:Throwable => Future.failed(e)
          }
        case Left(e) => Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(e)))
      }
  }
  def startStopEnrollment() = AuthorizationAction.async(parse.json) {
    request =>
      val tournamentID = request.body.\("_id").validate[String].asEither
      tournamentID match {
        case Right(id) =>
          val query = new Query(Criteria where "_id" is BSONObjectID(id))
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
      val query = new Query(Criteria where "_id" is BSONObjectID(id))
      val tournament = repository.find(query).get(ListEnum.head)
      val tournamentJson = tournament.toJson
      Future.successful(Ok(tournamentJson));
  }
}

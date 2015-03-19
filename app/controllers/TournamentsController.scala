package controllers

import java.util

import controllers.security.{TokenImpl, AuthorizationAction}
import models.strategy.strategies.SingleEliminationStrategy
import models.tournament.tournamentfields.BeforeEnrollment
import models.tournament.tournamentstate.{TournamentDescription, TournamentStaff, TournamentProperties}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import repositories.TournamentRepository
import models.tournament.tournamentstate.JsonFormatTournamentProperties._
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
      val tournamentProperties = request.body.validate[TournamentProperties].get
      val userID = TokenImpl(request.headers.get("token").get).getUserID
      val tournamentStaff =  new TournamentStaff(userID, new util.ArrayList())
      val beforeEnrollment = BeforeEnrollment(tournamentProperties, new SingleEliminationStrategy(), tournamentStaff)
      //mały fuckup żeby sprawdzić czy działa...
      val tournament = beforeEnrollment.startNext()
      try {
        repository.insert(tournament)
        Future.successful(Created)
      } catch {
        case e:IllegalArgumentException => Future.successful(UnprocessableEntity("Tournament can't be saved!"))
        case e:Throwable => Future.failed(e)
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
}

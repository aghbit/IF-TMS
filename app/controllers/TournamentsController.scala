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
import models.tournament.tournamentstate.JsonFormatTournamentDescription._
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
      repository.insert(beforeEnrollment)
      Future.successful(Created)
  }

  def getMyTournaments = AuthorizationAction.async {
    request =>
      val userID = TokenImpl(request.headers.get("token").get).getUserID
      val query = new Query(Criteria where "staff.admin" is userID)
      val tournaments = repository.find(query)
      val tournamentsProperties = tournaments.map(tournament => tournament.properties)
      Future.successful(Ok(Json.toJson(tournamentsProperties)))
  }
}

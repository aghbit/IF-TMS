package controllers

import java.util

import controllers.security.{TokenImpl, AuthorizationAction}
import models.strategy.strategies.SingleEliminationStrategy
import models.tournament.tournamentfields.BeforeEnrollment
import models.tournament.tournamentstate.{TournamentDescription, TournamentStaff, TournamentProperties}
import play.api.mvc.{Action, Controller}
import repositories.TournamentRepository
import models.tournament.tournamentstate.JsonFormatTournamentProperties._
import models.tournament.tournamentstate.JsonFormatTournamentDescription._

import scala.concurrent.Future

/**
 * Created by Szymek.
 */
object TournamentsController extends Controller{

  private val repository = new TournamentRepository()

  def createTournament() = AuthorizationAction.async(parse.json) {
    request =>
      println(request.body.toString())
      val tournamentProperties = request.body.validate[TournamentProperties].get
      val userID = TokenImpl(request.headers.get("token").get).getUserID
      val tournamentStaff =  new TournamentStaff(userID, new util.ArrayList())
      val beforeEnrollment = BeforeEnrollment(tournamentProperties, new SingleEliminationStrategy(), tournamentStaff)
      repository.insert(beforeEnrollment)
      Future.successful(Created)
  }
}

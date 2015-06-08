package models.tournament.tournamentstates

import java.util

import models.strategy.{EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties, TournamentSettings}
import models.user.User
import org.bson.types.ObjectId

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class DuringTournament(override val _id: ObjectId,
                       override var properties: TournamentProperties,
                       override var teams: util.ArrayList[Team],
                       override val staff: TournamentStaff,
                       override var strategy: EliminationStrategy) extends Tournament {

  override def startNext(): AfterTournament = {
    val newState = new AfterTournament(this._id, this.properties, this.teams, staff, strategy)
    newState
  }

  override def addReferee(user: User): Unit = {
    // void
  }

  override def removeReferee(user: User): Unit = {
    // void
  }

  override def editSettings(settings: TournamentSettings): Unit = {
    // void
  }

  override def addTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't add teams during this tournament phase")
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit term during this tournament phase")
  }

  override def removeTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't remove teams during this tournament phase")
  }
}

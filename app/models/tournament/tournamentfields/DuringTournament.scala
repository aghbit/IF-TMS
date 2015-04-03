package models.tournament.tournamentfields

import models.strategy.TournamentStrategy
import models.team.Team
import models.tournament.tournaments.Tournament
import models.tournament.tournamentstate.{TournamentStaff, TournamentTerm, TournamentProperties, TournamentSettings}
import models.user.User
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class DuringTournament(override val _id: BSONObjectID,
                       override var properties: TournamentProperties,
                       override var teams: ListBuffer[BSONObjectID],
                       override val strategy: TournamentStrategy,
                       override val staff: TournamentStaff) extends Tournament {

  override def startNext(): AfterTournament = {
    val newState = new AfterTournament(this._id, this.properties, this.teams, strategy, staff)
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

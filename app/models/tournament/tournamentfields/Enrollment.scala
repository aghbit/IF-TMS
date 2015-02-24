package models.tournament.tournamentfields

import models.strategy.TournamentStrategy
import models.team.Team
import models.tournament.tournaments.Tournament
import models.tournament.tournamentstate.{TournamentStaff, TournamentTerm, TournamentProperties}
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class Enrollment(override val _id: BSONObjectID,
                 override var properties: TournamentProperties,
                 override var teams: ListBuffer[BSONObjectID],
                 override val strategy: TournamentStrategy,
                 override val staff: TournamentStaff) extends Tournament {

  override def startNext(): Break = {
    val newState = new Break(this._id, this.properties, this.teams, strategy, staff)
    newState.properties.settings.canEnroll = false
    newState
  }

  override def addTeam(team: Team): Unit = {
    teams.append(team._id)
  }

  override def removeTeam(team: Team): Unit = {
    if (!teams.contains(team._id))
      throw new NoSuchElementException("Can't remove absent team from the Tournament!")
    teams = teams.filter(id => id != team._id)
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit term during this tournament phase")
  }
}
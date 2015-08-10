package models.tournament.tournamentstates

import java.util

import models.strategy.{EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * Created by Przemek
 */
class Enrollment(override val _id: ObjectId,
                 override var properties: TournamentProperties,
                 override var teams: util.ArrayList[Team],
                 override val staff: TournamentStaff,
                  override var strategy: EliminationStrategy,
                 override val discipline: TournamentType) extends Tournament {

  override def startNext(): Break = {
    val newState = new Break(this._id, this.properties, this.teams, staff, strategy, discipline)
    newState.properties.settings.canEnroll = false
    newState
  }

  override def addTeam(team: Team): Unit = {
    teams.add(team)
  }

  override def removeTeam(team: Team): Unit = {
    if (!teams.contains(team))
      throw new NoSuchElementException("Can't remove absent team from the Tournament!")
    teams.remove(team)
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit term during this tournament phase")
  }
}
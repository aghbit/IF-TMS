package models.tournament.tournamentstates

import java.util

import models.strategy.{EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties, TournamentSettings}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * Created by Przemek ..
 */
class Break(override val _id: ObjectId,
            override var properties: TournamentProperties,
            override var teams: util.ArrayList[Team],
            override val staff: TournamentStaff,
            override var strategy: EliminationStrategy,
            override val discipline: TournamentType) extends Tournament {

  override def startNext(): DuringTournament = {
    val newState = new DuringTournament(this._id, this.properties, this.teams, staff, strategy, discipline)
    newState
  }

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
  }

  override def removeTeam(team: Team): Unit = {
    if (!teams.contains(team))
      throw new NoSuchElementException("Can't remove absent team from the Tournament!")
    teams.remove(team)
  }

  override def addTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't add teams during this tournament phase")
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit terms during this tournament phase")
  }
}

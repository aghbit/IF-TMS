package models.tournament.tournamentstates

import java.util

import models.strategy.TournamentStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties, TournamentSettings}
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * Created by Przemek ..
 */
class Break(override val _id: BSONObjectID,
            override var properties: TournamentProperties,
            override var teams: util.ArrayList[BSONObjectID],
            override val strategy: TournamentStrategy,
            override val staff: TournamentStaff) extends Tournament {

  override def startNext(): DuringTournament = {
    val newState = new DuringTournament(this._id, this.properties, this.teams, strategy, staff)
    newState
  }

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
  }

  override def removeTeam(team: Team): Unit = {
    if (!teams.contains(team._id))
      throw new NoSuchElementException("Can't remove absent team from the Tournament!")
    teams.remove(team._id)
  }

  override def addTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't add teams during this tournament phase")
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit terms during this tournament phase")
  }
}

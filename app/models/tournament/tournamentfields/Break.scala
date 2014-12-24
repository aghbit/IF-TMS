package models.tournament.tournamentfields

import models.team.Team
import models.tournament.tournamentstate.{TournamentSettings, TournamentProperties, TournamentDiscipline}
import TournamentDiscipline.Discipline
import models.tournament.tournaments.Tournament
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek ..
 */
class Break(override val _id: BSONObjectID,
            override var properties: TournamentProperties,
            override var teams: ListBuffer[BSONObjectID],
            override val discipline: Discipline) extends Tournament{

  override def startNext(): DuringTournament = {
    val newState = new DuringTournament(this._id, this.properties, this.teams, this.discipline)
    newState
  }

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
  }
  override def removeTeam(team: Team): Unit = {
    if(!teams.contains(team._id))
      throw new NoSuchElementException("Can't remove absent team from the Tournament!")
      teams = teams.filter(id => id!= team._id)
  }
}

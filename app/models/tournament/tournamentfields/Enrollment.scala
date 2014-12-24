package models.tournament.tournamentfields

import models.team.Team
import models.tournament.tournamentstate.{TournamentProperties, TournamentDiscipline}
import TournamentDiscipline.Discipline
import models.tournament.tournaments.Tournament
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class Enrollment(override val _id: BSONObjectID,
                 override var properties: TournamentProperties,
                 override var teams: ListBuffer[BSONObjectID],
                 override val discipline: Discipline) extends Tournament{

  override def startNext(): Break = {
    val newState = new Break(this._id, this.properties, this.teams, this.discipline)
    newState.properties.settings.canEnroll = false
    newState
  }

  override def addTeam(team: Team): Unit = {
    teams.append(team._id)
  }

  override def removeTeam(team: Team): Unit = {
    if(!teams.contains(team._id))
      throw new NoSuchElementException("Can't remove absent team from the Tournament!")
    teams = teams.filter(id => id!= team._id)
  }
}

package models.tournament.tournaments

import models.team.Team
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek ..
 */
class Break(override val _id: BSONObjectID,
            override var properties: TournamentProperties,
            override var teams: ListBuffer[BSONObjectID],
            override val discipline: TournamentDiscipline) extends Tournament{

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
  }
  override def removeTeam(team: Team): Unit = {
    if(!teams.contains(team._id))
      throw new NoSuchElementException("Can't remove absent team from the Tournament!")
      teams = teams.filter(id => id!= team._id)
  }
}

package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import reactivemongo.bson.BSONObjectID
/**
 * Created by Szymek.
 */

class BeachVolleyballTeam(val _id:BSONObjectID,
                           val name:String,
                           val playersNumber:Int) extends VolleyballTeams {

  override val playersLimit: Int = 3

  override def canAddPlayer: Boolean = playersID.length<playersLimit
}

object BeachVolleyballTeam {
  def apply(name:String):BeachVolleyballTeam = {
    new BeachVolleyballTeam(BSONObjectID.generate, name, 2)
  }
}
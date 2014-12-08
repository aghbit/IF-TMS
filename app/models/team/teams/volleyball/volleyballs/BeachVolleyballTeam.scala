package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import reactivemongo.bson.BSONObjectID
/**
 * Created by Szymek.
 */

class BeachVolleyballTeam(val _id:BSONObjectID,
                           val name:String,
                           val playersNumber:Int) extends VolleyballTeams {

}

object BeachVolleyballTeam {
  def apply(name:String):BeachVolleyballTeam = {
    new BeachVolleyballTeam(BSONObjectID.generate, name, 2)
  }
}
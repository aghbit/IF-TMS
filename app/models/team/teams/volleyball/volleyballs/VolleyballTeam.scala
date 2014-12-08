package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class VolleyballTeam( val _id:BSONObjectID,
                      val name:String,
                      val playersNumber:Int) extends VolleyballTeams {

}
object VolleyballTeam{
  def apply(name:String):VolleyballTeam = {
    new VolleyballTeam(BSONObjectID.generate, name, 6)
  }
}
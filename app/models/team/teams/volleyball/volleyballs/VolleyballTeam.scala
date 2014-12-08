package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class VolleyballTeam( val _id:BSONObjectID,
                      val name:String,
                      val playersNumber:Int) extends VolleyballTeams {
  override val playersLimit: Int = 12

  override def canAddPlayer: Boolean = playersID.length<playersLimit
}
object VolleyballTeam{
  def apply(name:String):VolleyballTeam = {
    new VolleyballTeam(BSONObjectID.generate, name, 6)
  }
}
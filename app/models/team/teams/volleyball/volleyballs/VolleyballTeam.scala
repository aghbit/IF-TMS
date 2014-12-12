package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import reactivemongo.bson.BSONObjectID

import scala.collection.immutable.List

/**
 * Created by Szymek.
 */
class VolleyballTeam( val _id:BSONObjectID,
                      val name:String,
                      val playersNumber:Int,
                      val benchWarmersNumber:Int) extends VolleyballTeams {

}
object VolleyballTeam{

  /**
   * This constructor is only for amateur tournaments, in which more bench warmers
   * can play. In professional volleyball tournaments team contains only 6 players
   * and 6 bench warmers. It is additional and shouldn't be used now (so is private).
   */
  private def apply(name:String, benchWarmersNumber:Int) = {
    new VolleyballTeam(BSONObjectID.generate, name, 6, benchWarmersNumber)
  }

  def apply(name:String):VolleyballTeam = {
    apply(name, 6)
  }
}
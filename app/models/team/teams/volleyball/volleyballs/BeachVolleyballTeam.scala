package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import org.bson.types.ObjectId

/**
 * Created by Szymek
 */

class BeachVolleyballTeam(val _id: ObjectId,
                          val name: String,
                          val playersNumber: Int,
                          val benchWarmersNumber: Int) extends VolleyballTeams {


  def this(id:ObjectId, name:String) = this(id, name, 0, 0)


}

object BeachVolleyballTeam extends TeamObject{

  /**
   * This constructor is only for amateur tournaments, in which some bench warmers
   * can play. In professional beach volleyball tournaments team contains only 2 players
   * and 0 bench warmers. It is additional and shouldn't be used now (so is private).
   */
  private def apply(name: String, benchWarmersNumber: Int) = {
    new BeachVolleyballTeam(ObjectId.get, name, 2, benchWarmersNumber)
  }

  override def apply(name: String): BeachVolleyballTeam = {
    apply(name, 0)
  }
}
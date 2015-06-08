package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import org.bson.types.ObjectId

/**
 * Created by Szymek.
 */
class VolleyballTeam(val _id: ObjectId,
                     val name: String,
                     val playersNumber: Int,
                     val benchWarmersNumber: Int) extends VolleyballTeams {

  /**
   * Only for Spring Data. Don't use it. For more information check: TMS-76
   */
  def this() = this(null, null, 0, 0)

}

object VolleyballTeam extends TeamObject{

  /**
   * This constructor is only for amateur tournaments, in which more bench warmers
   * can play. In professional volleyball tournaments team contains only 6 players
   * and 6 bench warmers. It is additional and shouldn't be used now (so is private).
   */
  private def apply(name: String, benchWarmersNumber: Int) = {
    new VolleyballTeam(ObjectId.get, name, 6, benchWarmersNumber)
  }

  override def apply(name: String): VolleyballTeam = {
    apply(name, 6)
  }
}
package models.team.teams.volleyball.volleyballs

import models.team.teams.volleyball.VolleyballTeams
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek
 */

class BeachVolleyballTeam(val _id: BSONObjectID,
                          val name: String,
                          val playersNumber: Int,
                          val benchWarmersNumber: Int) extends VolleyballTeams {

}

object BeachVolleyballTeam {

  /**
   * This constructor is only for amateur tournaments, in which some bench warmers
   * can play. In professional beach volleyball tournaments team contains only 2 players
   * and 0 bench warmers. It is additional and shouldn't be used now (so is private).
   */
  private def apply(name: String, benchWarmersNumber: Int) = {
    new BeachVolleyballTeam(BSONObjectID.generate, name, 2, benchWarmersNumber)
  }

  def apply(name: String): BeachVolleyballTeam = {
    apply(name, 0)
  }
}
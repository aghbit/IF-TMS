package models.strategy.scores.newscores.volleyball

import models.strategy.scores.newscores.{PointsContainerCompanionObject, Points}
import play.api.libs.json.Json

/**
 * Created by Szymek Seget on 02.10.15.
 */
class VolleyballTieBreak(override var hostPoints:Int,
                         override var guestPoints:Int
                          ) extends Points(Some(15), true){

  override def toJson = {
    super.toJson + ("type" -> Json.toJson("VolleyballTieBreak"))
  }
}
object VolleyballTieBreak extends PointsContainerCompanionObject {
  override def build() = new VolleyballTieBreak(0,0)
}
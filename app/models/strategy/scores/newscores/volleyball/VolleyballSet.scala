package models.strategy.scores.newscores.volleyball

import models.strategy.scores.newscores.{PointsContainerCompanionObject, Points}
import play.api.libs.json.Json

/**
 * Created by Szymek Seget on 02.10.15.
 */
class VolleyballSet(override var hostPoints:Int,
                    override var guestPoints:Int
                     ) extends Points(Some(25), true){
  override def toJson = {
    super.toJson + ("type" -> Json.toJson("VolleyballSet"))
  }
}
object VolleyballSet extends PointsContainerCompanionObject{
  override def build() = new VolleyballSet(0,0)
}
package models.strategy.scores.newscores.speedminton

import models.strategy.scores.newscores.{PointsContainerCompanionObject, Points}
import play.api.libs.json.Json

/**
 * Created by Szymek Seget on 02.10.15.
 */
class SpeedmintonSet(override var hostPoints:Int,
                     override var guestPoints:Int
                      ) extends Points(Some(15), true){

  override def toJson = {
    super.toJson + ("type" -> Json.toJson("SpeedmintonSet"))
  }
}
object SpeedmintonSet extends PointsContainerCompanionObject{
  override def build() = new SpeedmintonSet(0,0)
}
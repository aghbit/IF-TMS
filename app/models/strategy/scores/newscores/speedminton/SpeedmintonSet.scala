package models.strategy.scores.newscores.speedminton

import models.strategy.scores.newscores.{PointsContainerCompanionObject, Points}

/**
 * Created by Szymek Seget on 02.10.15.
 */
class SpeedmintonSet(override var hostPoints:Int,
                     override var guestPoints:Int
                      ) extends Points(Some(15), true){

}
object SpeedmintonSet extends PointsContainerCompanionObject{
  override def build() = new SpeedmintonSet(0,0)
}
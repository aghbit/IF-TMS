package models.strategy.scores.newscores.volleyball

import models.strategy.scores.newscores.{PointsContainerCompanionObject, Points}

/**
 * Created by Szymek Seget on 02.10.15.
 */
class VolleyballSet(override var hostPoints:Int,
                    override var guestPoints:Int
                     ) extends Points(Some(25), true){

}
object VolleyballSet extends PointsContainerCompanionObject{
  override def build() = new VolleyballSet(0,0)
}
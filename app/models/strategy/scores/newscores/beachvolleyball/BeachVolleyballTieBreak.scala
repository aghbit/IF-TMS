package models.strategy.scores.newscores.beachvolleyball

import models.strategy.scores.newscores.{PointsContainerCompanionObject, Points}

/**
 * Created by Szymek Seget on 02.10.15.
 */
class BeachVolleyballTieBreak(override var hostPoints:Int,
                              override var guestPoints:Int
                               ) extends Points(Some(15), true){

}
object BeachVolleyballTieBreak extends PointsContainerCompanionObject{
  override def build() = new BeachVolleyballTieBreak(0,0)
}
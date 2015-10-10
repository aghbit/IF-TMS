package models.strategy.scores.newscores.beachvolleyball

import models.strategy.scores.newscores.{PointsContainerCompanionObject, Points}

/**
 * Created by Szymek Seget on 02.10.15.
 */
class BeachVolleyballSet(override var hostPoints:Int,
                         override var guestPoints:Int
                          ) extends Points(Some(21), true){
}
object BeachVolleyballSet extends PointsContainerCompanionObject {
  override def build() = new BeachVolleyballSet(0,0)
}

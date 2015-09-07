package models.strategy.scores

import models.strategy.Score
import play.api.libs.json.JsObject

/**
 * Created by Szymek Seget on 07.09.15.
 */
class SpeedmintonScore extends Score{


  override def isHostWinner: Boolean = ???

  override def setScoreInLastSet(hostScore: Int, guestScore: Int): Unit = ???

  override def isMatchFinished: Boolean = ???

  override def addSet(): Unit = ???

  override def isGuestWinner: Boolean = ???

  /**
   * Return JsObject representation. e.g.
   * {"score": {
   * sets: [{"1": {
   * "host":21,
   * "guest":15}},
   * {"2": {
   * "host":21,
   * "guest":10}},
   * {"3": {
   * "host":null,
   * "guest":null}}
   * ]
   * }
   * }
   * @return
   */
  override def toJson: JsObject = ???
}

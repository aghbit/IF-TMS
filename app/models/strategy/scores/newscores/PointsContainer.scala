package models.strategy.scores.newscores

import play.api.libs.json.JsObject

/**
 * Created by Szymek Seget on 27.09.15.
 */
trait PointsContainer {

  def isHostWinner:Boolean
  def isGuestWinner:Boolean
  def isDraw:Boolean
  def toJson:JsObject
}

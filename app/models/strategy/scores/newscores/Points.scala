package models.strategy.scores.newscores

import play.api.libs.json.{JsObject, Json}

/**
 * Created by Szymek Seget on 27.09.15.
 */
abstract class Points(val maxPoints:Option[Int],
              val winByTwoPoints:Boolean) extends PointsContainer{

  var hostPoints:Int
  var guestPoints:Int

  override def isHostWinner: Boolean = {
    val winPointsDistance = winByTwoPoints match {
      case true => 2
      case false => 1
    }
    maxPoints match {
      case Some(max) =>
        (hostPoints>max && hostPoints == (guestPoints+winPointsDistance)) ||
          (hostPoints == max && hostPoints >= (guestPoints+winPointsDistance))
      case None => hostPoints >= guestPoints+winPointsDistance
    }

  }

  override def isGuestWinner: Boolean = {
    val winPointsDistance = winByTwoPoints match {
      case true => 2
      case false => 1
    }
    maxPoints match {
      case Some(max) =>
        (guestPoints>max && guestPoints == (hostPoints+winPointsDistance)) ||
          (guestPoints == max && guestPoints >= (hostPoints+winPointsDistance))
      case None => guestPoints >= hostPoints+winPointsDistance
    }

  }

  override def isDraw: Boolean = hostPoints == guestPoints

  override def toJson: JsObject = {
    Json.obj(
      "host" -> hostPoints,
      "guest" -> guestPoints
    )
  }
}

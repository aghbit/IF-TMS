package models.strategy.scores

import models.strategy.Score
import play.api.libs.json.{Json, JsObject}

/**
 * Created by Szymek Seget on 24.05.15.
 */
class BeachVolleyballScore(
                          var firstSetHostScore:Option[Int],
                          var firstSetGuestScore:Option[Int],
                          var secondSetHostScore:Option[Int],
                          var secondSetGuestScore:Option[Int],
                          var thirdSetHostScore:Option[Int],
                          var thirdSetGuestScore:Option[Int]) extends Score{

  def firstSetHostScore(points:Int):BeachVolleyballScore = {
    require(validPoints(points), "Number of points has to be positive.")
    firstSetHostScore = Some(points)
    this
  }

  def firstSetGuestScore(points:Int):BeachVolleyballScore = {
    require(validPoints(points), "Number of points has to be positive.")
    firstSetGuestScore = Some(points)
    this
  }

  /**
   * Checks first set state. If first set has finished, returns true, otherwise
   * returns false
   * @return Boolean
   */
  private def isFirstSetFinished:Boolean = {
    (firstSetGuestScore, firstSetHostScore) match {
      case (Some(g), Some(h)) => isSetFinished(h,g)
      case _ => false
    }
  }

  def secondSetHostScore(points:Int):BeachVolleyballScore = {
    require(validPoints(points), "Number of points has to be positive.")
    if(!isFirstSetFinished){
      throw new IllegalArgumentException("Can't set second set score, while first has not finished.")
    }
    secondSetHostScore = Some(points)
    this
  }

  def secondSetGuestScore(points:Int):BeachVolleyballScore = {
    require(validPoints(points), "Number of points has to be positive.")
    if(!isFirstSetFinished){
      throw new IllegalArgumentException("Can't set second set score, while first has not finished.")
    }
    secondSetGuestScore = Some(points)
    this
  }

  /**
   * Checks second set state. If second set has finished, returns true, otherwise
   * returns false
   * @return Boolean
   */
  private def isSecondSetFinished:Boolean = {
    if(!isFirstSetFinished){
      false
    }else {
      (secondSetHostScore, secondSetGuestScore) match {
        case (Some(h), Some(g)) => isSetFinished(h,g)
        case _ => false
      }
    }
  }

  def thirdSetHostScore(points:Int):BeachVolleyballScore = {
    require(validPoints(points), "Number of points has to be positive.")
    if(!isSecondSetFinished){
      throw new IllegalArgumentException("Can't set second set score, while first has not finished.")
    }
    thirdSetHostScore = Some(points)
    this
  }

  def thirdSetGuestScore(points:Int):BeachVolleyballScore = {
    require(validPoints(points), "Number of points has to be positive.")
    if(!isSecondSetFinished){
      throw new IllegalArgumentException("Can't set second set score, while first has not finished.")
    }
    thirdSetGuestScore = Some(points)
    this
  }

  /**
   * Checks points. Returns true if points belong to [0;21]
   * @param points - Int
   * @return true if valid or false
   */
  def validPoints(points:Int) = {
    points>=0
  }

  /**
   * Returns true, if match has finished. Otherwise returns false.
   * @return Boolean
   */
  def isMatchFinished:Boolean = {
    if(!isSecondSetFinished){
      false
    }else {
      (thirdSetGuestScore, thirdSetHostScore) match {
        case (Some(g), Some(h)) => isTieBreakFinished(h,g)
        case _ => false
      }
    }
  }

  private def isSetFinished(h: Int, g: Int): Boolean = (h>=21 && g<=h-2) || (g>=21 && h<=g-2)

  private def isTieBreakFinished(h: Int, g: Int): Boolean = (h>=15 && g<=h-2) || (g>=15 && h<=g-2)

  /**
   * Return JsObject representation. e.g.
   * {"score": {
   *          sets: [{"1": {
   *                        "host":21,
   *                        "guest":15}},
   *                 {"2": {
   *                        "host":21,
   *                        "guest":10}},
   *                 {"3": {
   *                        "host":null,
   *                        "guest":null}}
   *                ]
   *           }
   * }
   * @return
   */
  override def toJson: JsObject = {
    Json.obj("score" ->
      Json.obj("sets" ->
      Json.arr(Json.obj("1" ->
        Json.obj("host" -> firstSetHostScore,
                 "guest" -> firstSetGuestScore)),
      Json.obj("2" ->
        Json.obj("host" -> secondSetHostScore,
                 "guest" -> secondSetGuestScore)),
      Json.obj("3" ->
        Json.obj("host" -> thirdSetHostScore,
                 "guest" -> thirdSetGuestScore)
      )))
    )
  }
}

object BeachVolleyballScore {
  def apply() = new BeachVolleyballScore(None, None, None, None, None, None)
}

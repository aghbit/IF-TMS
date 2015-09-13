package models.strategy.scores

import models.strategy.Score
import play.api.libs.json.{JsNull, Json, JsObject}

/**
 * Created by Szymek Seget on 07.09.15.
 */
class SpeedmintonScore extends Score {


  /**
   * Stores scores in every set as tuple. Tuple structure:
   * (hostScore, guestScore)
   */
  var sets = scala.collection.mutable.Map[Int, (Int, Int)]()
  var numberOfSets = 0
  var hostSets = 0
  var guestSets = 0

  /**
   * Adds new set with start score 0:0 to end of the sets list.
   * First set's key is 1.
   */
  override def addSet() = {
    if (numberOfSets == 3)
      throw new IllegalStateException("Can't add forth set in beach volleyball match. ")
    numberOfSets = numberOfSets + 1
    sets += (numberOfSets ->(0, 0))
  }

  override def setScoreInLastSet(hostScore: Int, guestScore: Int) = {
    sets.remove(numberOfSets)
    sets += (numberOfSets ->(hostScore, guestScore))
    if (isNthSetFinished(numberOfSets) && isHostWinnerOfNthSet(numberOfSets)) {
      hostSets = hostSets + 1
    } else if (isNthSetFinished(numberOfSets)) {
      guestSets = guestSets + 1
    }
  }

  private def isHostWinnerOfNthSet(n: Int): Boolean = {
    require(n <= numberOfSets && n >= 1, "Wrong number of set")
    val hostScore = sets.get(n).get._1
    val guestScore = sets.get(n).get._2

    (hostScore == 16 && guestScore <= 14) || (hostScore > 16 && guestScore == hostScore - 2)

  }

  override def isHostWinner(): Boolean = {
    isMatchFinished() && hostSets.equals(2)
  }

  /**
   * Return JsObject representation. e.g.
   * {"score": [{"1": {
   * "host":16,
   * "guest":14}},
   * {"2": {
   * "host":16,
   * "guest":10}}
   * ]
   * }
   * @return
   */
  override def toJson: JsObject = {
    var i = 1
    var list: List[JsObject] = List()
    while (i <= sets.size) {
      val js = Json.obj(i.toString -> (sets.get(i) match {
        case Some(x: (Int, Int)) => {
          Json.obj("host" -> x._1,
            "guest" -> x._2)
        }
        case None => Json.obj("host" -> JsNull, "guest" -> JsNull)
      }))
      list = list ::: List(js)
      i = i + 1
    }
    Json.obj("sets" -> list)
  }

  override def isGuestWinner(): Boolean = {
    isMatchFinished() && guestSets.equals(2)
  }

  override def isMatchFinished(): Boolean = {
    hostSets.equals(2) || guestSets.equals(2)
  }

  private def isNthSetFinished(n: Int): Boolean = {
    require(n <= numberOfSets && n >= 1, "Wrong number of set")
    val hostScore = sets.get(n).get._1
    val guestScore = sets.get(n).get._2

    (hostScore.equals(16) && guestScore <= 14) ||
      (hostScore <= 14 && guestScore.equals(16)) ||
      (hostScore > 16 && hostScore.equals(guestScore + 2)) ||
      ((hostScore + 2).equals(guestScore) && guestScore > 16)
  }
}

object SpeedmintonScore {
  def apply() = new BeachVolleyballScore()
}

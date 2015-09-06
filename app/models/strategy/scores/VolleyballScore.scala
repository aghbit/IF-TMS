package models.strategy.scores

import models.strategy.Score
import play.api.libs.json.{JsNull, Json, JsObject}

/**
 * Created by Szymek Seget on 12.08.15.
 */
class VolleyballScore extends Score{

  var sets = scala.collection.mutable.Map[Int, (Int, Int)]()
  var numberOfSets = 0
  private var hostSetsNumber = 0
  private var guestSetsNumber = 0

  override def isHostWinner(): Boolean = {
    isMatchFinished() && hostSetsNumber.equals(3)
  }

  override def setScoreInLastSet(hostScore: Int, guestScore: Int): Unit = {
    sets.remove(numberOfSets)
    sets += (numberOfSets -> (hostScore, guestScore))
    if(isNthSetFinished(numberOfSets) && isHostWinnerOfNthSet(numberOfSets)){
      hostSetsNumber = hostSetsNumber + 1
    }else if(isNthSetFinished(numberOfSets)){
      guestSetsNumber = guestSetsNumber + 1
    }
  }

  override def addSet(): Unit = {
    if(numberOfSets==5)
      throw new IllegalStateException("Can't add more then five sets in volleyball match.")
    numberOfSets = numberOfSets + 1
    sets += (numberOfSets -> (0, 0))
  }

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
  override def toJson: JsObject = {
    var i = 1
    var list:List[JsObject] = List()
    while(i<=sets.size){
      val js = Json.obj(i.toString -> (sets.get(i) match {
        case Some(x:(Int,Int)) => {
          Json.obj("host" -> x._1,
            "guest" -> x._2)
        }
        case None => Json.obj("host"-> JsNull, "guest"->JsNull)
      }))
      list = list ::: List(js)
      i = i+1
    }
    Json.obj("sets" -> list)
  }

  override def isGuestWinner(): Boolean = {
    isMatchFinished() && guestSetsNumber.equals(3)
  }

  override def isMatchFinished(): Boolean = {
    hostSetsNumber.equals(3) || guestSetsNumber.equals(3)
  }

  private def isNthSetFinished(n:Int): Boolean = {
    require(n<=numberOfSets && n>=1, "Wrong number of set")
    val hostScore = sets.get(n).get._1
    val guestScore = sets.get(n).get._2
    if(n<5){
      (hostScore.equals(25) && guestScore<=23) ||
        (hostScore<=23 && guestScore.equals(25)) ||
        (hostScore>25 && hostScore.equals(guestScore+2)) ||
        ((hostScore+2).equals(guestScore) && guestScore>25)
    }else {
      (hostScore.equals(15) && guestScore<=13) ||
        (hostScore<=13 && guestScore.equals(15)) ||
        (hostScore>15 && hostScore.equals(guestScore+2)) ||
        ((hostScore+2).equals(guestScore) && guestScore>15)
    }
  }

  private def isHostWinnerOfNthSet(n:Int) = {
    val hostScore = sets.get(n).get._1
    val guestScore = sets.get(n).get._2
    if(n<5) {
      (hostScore.equals(25) && guestScore <= 23) || (hostScore > 25 && hostScore.equals(guestScore + 2))
    }else {
      (hostScore.equals(15) && guestScore <= 13) || (hostScore > 15 && hostScore.equals(guestScore + 2))
    }
  }
}
object VolleyballScore {
  def apply() = new VolleyballScore
}
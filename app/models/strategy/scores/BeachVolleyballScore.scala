package models.strategy.scores

import models.strategy.Score
import play.api.libs.json.{JsNull, Json, JsObject}


/**
 * Created by Szymek Seget on 24.05.15.
 */
class BeachVolleyballScore extends Score{

  /**
   * Stores scores in every set as tuple. Tuple structure:
   * (hostScore, guestScore)
   */
  var sets = scala.collection.mutable.Map[Int, (Int, Int)]()
  var numberOfSets = 0

  /**
   * Adds new set with start score 0:0 to end of the sets list.
   */
  override def addSet() = {
    if(numberOfSets==3)
      throw new IllegalStateException("Can't add forth set in beach volleyball match. ")
    numberOfSets = numberOfSets + 1
    sets += (numberOfSets -> (0, 0))
  }

  override def setScoreInLastSet(hostScore:Int, guestScore:Int) = {
    sets.remove(numberOfSets)
    sets += (numberOfSets -> (hostScore, guestScore))
  }

  private def isHostWinnerOfNthSet(n:Int): Boolean = {
    require(n<=numberOfSets && n>=1, "Wrong number of set")
    val hostScore = sets.get(n).get._1
    val guestScore = sets.get(n).get._2
    if(n<3){
      (hostScore == 21 && guestScore<=19) || (hostScore>21 && guestScore == hostScore-2)
    }else{
      (hostScore == 15 && guestScore<=13) || (hostScore>15 && guestScore == hostScore-2)
    }
  }

  override def isHostWinner():Boolean = {
    if(isHostWinnerOfNthSet(1) && isHostWinnerOfNthSet(2)){
      true
    }else if(!isHostWinnerOfNthSet(1) && !isHostWinnerOfNthSet(2)){
      false
    }else{
      isHostWinnerOfNthSet(3)
    }
  }
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
      Json.arr(Json.obj("1" -> (sets.get(1) match {
        case Some(x:(Int,Int)) => {
          Json.obj("host" -> x._1,
                    "guest" -> x._2)
        }
        case None => Json.obj("host"-> JsNull, "guest"->JsNull)
      })),
        Json.obj("2" -> (sets.get(2) match {
          case Some(x:(Int,Int)) => {
            Json.obj("host" -> x._1,
              "guest" -> x._2)
          }
          case None => Json.obj("host"-> JsNull, "guest"->JsNull)
        })),
        Json.obj("3" -> (sets.get(3) match {
          case Some(x:(Int,Int)) => {
            Json.obj("host" -> x._1,
              "guest" -> x._2)
          }
          case None => Json.obj("host"-> JsNull, "guest"->JsNull)
        }))))
    )
  }
}

object BeachVolleyballScore {
  def apply() = new BeachVolleyballScore()
}

package models.strategy.scores

import models.strategy.{Score, VSet}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-08.
 */
class VolleyballScore(override val host:Option[BSONObjectID],
                      override val guest:Option[BSONObjectID],
                      val maxSets:Int)extends Score {

  var hostSets: List[VSet] = List(VSet())
  var guestSets: List[VSet] = List(VSet())
  val maxPoints = 25

  def isMatchFinished:Boolean =
    if(hostSets.count(set => set.won)==maxSets || guestSets.count(set => set.won)==maxSets){
      true
    }
    else{
      false
    }

  def addPoint(scorer:Option[BSONObjectID]):Unit = {
     if(isMatchFinished) throw new MatchFinishedException("Adding points when match is finished is unable!")
     if(scorer==host){
       hostSets.last.points+=1
       if(isSetFinished) {
         hostSets.last.won=true
         hostSets = hostSets ++ List(VSet())
         guestSets = guestSets ++ List(VSet())
       }
     }else{
       guestSets.last.points+=1
       if(isSetFinished) {
         guestSets.last.won=true
         guestSets = hostSets ++ List(VSet())
         hostSets = hostSets ++ List(VSet())
       }
     }
  }

//  protected def isSetFinished[A <: VolleyballScore](opponentScore:A) = {
//    sets.last.points>=maxPoints && (sets.last.points-opponentScore.sets.last.points)>=2
//  }

  protected def isSetFinished = {
    (hostSets.last.points>=maxPoints || guestSets.last.points>=maxPoints) && Math.abs(hostSets.last.points-guestSets.last.points)>=2
  }


//  protected def isCorrectlyFinished(Other:Score) = {
//    if(!isEnded && !Other.isEnded) throw new MatchNotFinishedException("Match is not yet finished")
//  }

  override def isCorrectlyFinished = {
    if(hostSets.count(set => set.won)!=maxSets && guestSets.count(set => set.won)!=maxSets) throw new MatchNotFinishedException("Match is not yet finished")
  }




    override def getWinner:Option[BSONObjectID]={
      isCorrectlyFinished
      if(guestSets.count(set => set.won)==maxSets){
        guest
      } else {
        host
      }
    }

    override def getLoser:Option[BSONObjectID]={
      if(getWinner==host){
        guest
      } else {
        host
      }
    }
//  }
//  override def >[A <: Score](Other:Score):Boolean={
//    isCorrectlyFinished(Other)
//    Other match {
//      case other:A => isEnded && !other.isEnded
//      case _ => false
//    }
//  }
}

object VolleyballScore{
  def apply(host:Option[BSONObjectID],guest:Option[BSONObjectID],maxSets:Int = 3) = new VolleyballScore(host,guest,maxSets)
}

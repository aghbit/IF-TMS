package models.strategy.scores

import models.strategy.{Score, VSet}

/**
 * Created by Rafal on 2014-12-08.
 */
class VolleyballScore(val maxSets:Int)extends Score {
  var sets: List[VSet] = List(VSet())
  val maxPoints = 25

  def isEnded:Boolean = sets.filter(set => set.won).size==maxSets

  def addPoint[A <: VolleyballScore](opponentScore:A):Unit = {
     sets.last.points+=1
     if(isSetFinished(opponentScore)) {
       sets.last.won=true
       sets = sets ++ List(VSet())
       opponentScore.sets = opponentScore.sets ++ List(VSet())
     }
  }

  protected def isSetFinished[A <: VolleyballScore](opponentScore:A) = {
    sets.last.points>=maxPoints && (sets.last.points-opponentScore.sets.last.points)>=2
  }


  protected def isCorrectlyFinished(Other:Score): Unit = {
    if(!isEnded && !Other.isEnded) throw new MatchNotFinishedException("Match is not yet finished")
  }


  override def <[A <: Score] (Other:Score):Boolean={
      isCorrectlyFinished(Other)
      Other match {
        case other:A => other.isEnded && !isEnded
        case _ => false
      }
  }
  override def >[A <: Score](Other:Score):Boolean={
    isCorrectlyFinished(Other)
    Other match {
      case other:A => isEnded && !(other.isEnded)
      case _ => false
    }
  }
}

object VolleyballScore{
  def apply(maxSets:Int = 3) = new VolleyballScore(maxSets)
}

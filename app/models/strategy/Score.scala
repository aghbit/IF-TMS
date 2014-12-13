package models.strategy

import models.strategy.scores.MatchNotFinishedException

/**
 * Created by Rafal on 2014-12-07.
 */
trait Score {
  def isEnded:Boolean
  def <[A <: Score](Other:Score):Boolean
  def >[A <: Score](Other:Score):Boolean
}
object Score{
  def apply() = new Score {override def isEnded: Boolean = false
    override def >[A <: Score](Other: Score): Boolean = throw new MatchNotFinishedException("Match is not yet finished")
    override def <[A <: Score](Other: Score): Boolean = throw new MatchNotFinishedException("Match is not yet finished")
  }
}
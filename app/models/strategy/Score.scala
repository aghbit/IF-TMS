package models.strategy


import models.strategy.scores.MatchNotFinishedException
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-07.
 */
trait Score {
  val host:Option[BSONObjectID]=None
  val guest:Option[BSONObjectID]=None
  def isMatchFinished:Boolean
  def isCorrectlyFinished
  def getWinner:Option[BSONObjectID]
  def getLoser:Option[BSONObjectID]
}
object Score{
  def apply() = new Score {
    override def isCorrectlyFinished = throw new MatchNotFinishedException("Match is not yet finished")
    override def isMatchFinished: Boolean = false
    override def getLoser:Option[BSONObjectID] = throw new MatchNotFinishedException("Match is not yet finished")
    override def getWinner:Option[BSONObjectID] = throw new MatchNotFinishedException("Match is not yet finished")
  }
}
package models.strategy


import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-07.
 */
trait Score {
  val host:BSONObjectID
  val guest:BSONObjectID
  def isMatchFinished:Boolean
  def getWinner:Option[BSONObjectID]
  def getLoser:Option[BSONObjectID]
}

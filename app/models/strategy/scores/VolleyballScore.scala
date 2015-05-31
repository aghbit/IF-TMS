package models.strategy.scores

import models.exceptions.MatchFinishedException
import models.strategy.{Score, VSet}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-08.
 */
class VolleyballScore(override val host: BSONObjectID,
                      override val guest: BSONObjectID,
                      val maxSets: Int) extends Score {

  var hostSets: List[VSet] = List(VSet())
  var guestSets: List[VSet] = List(VSet())
  val maxPoints = 25

  def isMatchFinished: Boolean =
    if (hostSets.count(set => set.won) == maxSets / 2 + 1 || guestSets.count(set => set.won) == maxSets / 2 + 1) {
      true
    }
    else {
      false
    }

  def addPoint(scorer: BSONObjectID): Unit = {
    if (isMatchFinished) throw new MatchFinishedException("Adding points when match is finished is unable!")
    if (scorer == host) {
      hostSets.last.points += 1
      if (isSetFinished) {
        hostSets.last.won = true
        hostSets = hostSets ++ List(VSet())
        guestSets = guestSets ++ List(VSet())
      }
    } else {
      guestSets.last.points += 1
      if (isSetFinished) {
        guestSets.last.won = true
        guestSets = hostSets ++ List(VSet())
        hostSets = hostSets ++ List(VSet())
      }
    }
  }


  protected def isSetFinished = {
    (hostSets.last.points >= maxPoints || guestSets.last.points >= maxPoints) && Math.abs(hostSets.last.points - guestSets.last.points) >= 2
  }


  override def getWinner: Option[BSONObjectID] = {
    if (guestSets.count(set => set.won) == maxSets) {
      Some(guest)
    } else {
      Some(host)
    }
  }

  override def getLoser: Option[BSONObjectID] = {
    if (getWinner == Some(host)) {
      Some(guest)
    } else {
      Some(host)
    }
  }

}

object VolleyballScore {
  def apply(host: BSONObjectID, guest: BSONObjectID, maxSets: Int = 5) = new VolleyballScore(host, guest, maxSets)
}

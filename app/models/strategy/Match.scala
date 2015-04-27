package models.strategy

import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-02.
 */
class Match(val host: Option[BSONObjectID],
            val guest: Option[BSONObjectID]) {

  var score: Option[Score] = None

  def isMatchFinished: Boolean = (host, guest, score) match {
    case (None, _, _) => true
    case (_, None, _) => true
    case (_, _, None) => false
    case (_, _, score) => score.get.isMatchFinished
  }

  def losingTeam: Option[BSONObjectID] =
    if (guest == None) {
      guest
    } else
    if (host == None) {
      host
    } else {
      score.get.getLoser
    }


  def winningTeam: Option[BSONObjectID] =
    if (losingTeam == host) {
      guest
    } else {
      host
    }
}

object Match {
  def apply(host: Option[Team], guest: Option[Team]) = (host, guest) match {
    case (None, None) => new Match(None, None)
    case (None, Some(guest)) => new Match(None, Some(guest._id))
    case (Some(host), None) => new Match(Some(host._id), None)
    case (Some(host), Some(guest)) => new Match(Some(host._id), Some(guest._id))
  }
}
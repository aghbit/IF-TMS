package models.strategy

import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-02.
 */
class Match(val host:Option[BSONObjectID],
            val guest:Option[BSONObjectID]){

  var scoreHost:Score = Score()
  var scoreGuest:Score = Score()
  def isEnded:Boolean={
    if(host==None || guest==None){ true }
    else {
      if (scoreHost.isEnded || scoreGuest.isEnded) { true } else { false }
    }
  }
  def losingTeam:Option[BSONObjectID] =
    if(guest==None) guest
    else if(host==None) host
    else
      scoreGuest match{
        case score if score<scoreHost => guest
        case _ => host
      }

  def winningTeam:Option[BSONObjectID] =
    if(guest==None) host
    else if(host==None) guest
    else
      scoreGuest match{
        case score if score<scoreHost => host
        case _ => guest
      }
}
object Match {
  def apply(host:Option[Team], guest:Option[Team])= (host,guest) match {
    case (None,None) => new Match(None,None)
    case(None,Some(guest)) => new Match(None,Some(guest._id))
    case(Some(host),None) => new Match(Some(host._id),None)
    case(Some(host),Some(guest)) => new Match(Some(host._id),Some(guest._id))
  }
}
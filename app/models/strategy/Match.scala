package models.strategy

import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-02.
 */
class Match(val host:BSONObjectID,
            val guest:BSONObjectID){

  var scoreHost:Score = Score()
  var scoreGuest:Score = Score()
  def isEnded:Boolean={
    if(host==null || guest==null) true
    else
    if(scoreHost.isEnded || scoreGuest.isEnded) true else false
  }
  def losingTeam:BSONObjectID =
    if(guest==null) guest
    else if(host==null) host
    else
      scoreGuest match{
        case score if score<scoreHost => guest
        case _ => host
      }

  def winningTeam:BSONObjectID =
    if(guest==null) host
      else if(host==null) guest
    else
      scoreGuest match{
        case score if score<scoreHost => host
        case _ => guest
      }
}
object Match {
  def apply(host:Team, guest:Team)= (host,guest) match {
    case (null,null) => new Match(null,null)
    case(null,guest) => new Match(null,guest._id)
    case(host,null) => new Match(host._id,null)
    case(host,guest) => new Match(host._id,guest._id)
  }
}
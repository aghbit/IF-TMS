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
    if(scoreHost.isEnded || scoreGuest.isEnded) true else false
  }
  def losingTeam:BSONObjectID =
    if(guest==null) guest
    else
      scoreGuest match{
        case score if score<scoreHost => guest
        case _ => host
      }

  def winningTeam:BSONObjectID =
    if(guest==null) host
    else
      scoreGuest match{
        case score if score<scoreHost => host
        case _ => guest
      }
}
object Match {
  def apply(host:Team, guest:Team)= if(guest!=null) new Match(host._id,guest._id) else new Match(host._id,null)
}
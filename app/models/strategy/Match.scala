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

  def winningTeam:BSONObjectID ={
    if(scoreHost>scoreGuest) host
    else guest
  }
}
object Match {
  def apply(host:Team, guest:Team)= new Match(host._id,guest._id)
}
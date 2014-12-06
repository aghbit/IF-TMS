package models.strategy

import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-02.
 */
trait MatchStructure {
  def setScore(_id: Option[BSONObjectID],score:String):Unit
  def setTeams(id:Option[BSONObjectID],host:Team, guest:Team):Unit
}

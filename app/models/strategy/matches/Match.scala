package models.strategy.matches

import models.strategy.MatchStructure
import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-02.
 */
class Match(val _id: Option[BSONObjectID],
            val host:Team,
            val guest:Team,
            val score:String ) extends MatchStructure{


  override def setScore(id: Option[BSONObjectID],score:String): Unit = ???

  override def setTeams(id: Option[BSONObjectID], host: Team, guest: Team): Unit = ???
}

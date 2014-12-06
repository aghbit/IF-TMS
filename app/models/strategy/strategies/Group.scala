package models.strategy.strategies

import models.strategy.{TournamentStrategy, MatchStructure}
import models.strategy.matches.Match
import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-02.
 */
class Group() extends MatchStructure with TournamentStrategy{

  val column: List[(Team,String,Int)] = _
  override val ListOfTeams: List[Team] = _
  override val ListOfMatches: List[Match] = _
  override val View: MatchStructure = _

  override def draw[Option: String]: Unit = ???

  override def getView(): MatchStructure = ???

  override def getOrder(): List[Team] = ???

  override def setScore(id: Option[BSONObjectID],score:String): Unit = ???

  override def setTeams(id: Option[BSONObjectID], host: Team, guest: Team): Unit = ???

}

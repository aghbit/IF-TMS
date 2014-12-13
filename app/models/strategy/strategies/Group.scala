package models.strategy.strategies

import models.strategy.{Match, MatchStructure, Score, TournamentStrategy}
import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-02.
 */
class Group extends MatchStructure with TournamentStrategy{

  val column: List[(Team,String,Int)] = ???
  override val ListOfTeams: List[BSONObjectID] = ???


  override def getOrder(): List[Team] = ???

  override var matches: Map[Match,Int] = ???

  override def draw: Unit = ???

  override def attachNumberOfTeams(): Int = ???

  override def setScore(game: Match, score: Score): Unit = ???
}

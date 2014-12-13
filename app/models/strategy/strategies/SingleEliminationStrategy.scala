package models.strategy.strategies

import models.strategy.{Match, Score}
import models.strategy.scores.OneTeamMatchScore
import models.team.Team
import reactivemongo.bson.BSONObjectID

import scala.annotation.tailrec
import scala.util.Random

/**
 * Created by Szymek.
 */
class SingleEliminationStrategy(val ListOfTeams:List[BSONObjectID],
                                val isSeeding:Boolean) {

//
//  override def draw: Unit = {
//    /// not yet taking isSeeding into account
//    def drawMatch(teams: List[BSONObjectID], placesToFill: Int): Unit = {
//    }
//  }
//
//
//  override def attachNumberOfTeams: Int = {
//    @tailrec def compareAndReturn(n: Int): Int =
//      if (n >= ListOfTeams.size) n
//      else compareAndReturn(2 * n)
//    compareAndReturn(1)
//  }
//
//  override var matches: Map[Match, Int] = _
//
//  override def setScore(game: Match, score: Score): Unit = ???
//
//  override def getOrder(): List[Team] = ???
}

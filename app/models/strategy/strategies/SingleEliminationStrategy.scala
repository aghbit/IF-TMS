package models.strategy.strategies

import models.strategy.Tree.EliminationTree
import models.strategy.{TournamentStrategy, Match, Score}
import models.team.Team
import reactivemongo.bson.BSONObjectID

import scala.annotation.tailrec
import scala.util.Random

/**
 * Created by Szymek.edited by Ludwik
 */
 class SingleEliminationStrategy(val ListOfTeams:List[BSONObjectID]) extends TournamentStrategy {

  //Generates tree withh null in every leaf/branch
  override def generateTree(count: Integer): EliminationTree = {
    val num = attachNumberOfTeams;
    def log2(x: Double) = scala.math.log(x) / scala.math.log(2)
    //To get logaritm with base 2
    val deph = log2(num.toDouble).toInt;
    var root: EliminationTree = new EliminationTree()
    def
    tree
  }

   def attachNumberOfTeams: Int = {
    @tailrec def compareAndReturn(n: Int): Int =
      if (n >= ListOfTeams.size) n
      else compareAndReturn(2 * n)
    compareAndReturn(1)
  }

  override var tree: EliminationTree = _

  override def populateTree(list: List[Team]): Unit = ???

  override def getNextMatch(): Match = ???

  override def updateTree(m: Match): Unit = ???
}

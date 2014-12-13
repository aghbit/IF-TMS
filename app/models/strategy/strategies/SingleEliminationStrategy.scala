package models.strategy.strategies

import models.strategy.Tree.{Game, EliminationTree}
import models.strategy.{TournamentStrategy, Match}
import models.team.Team
import reactivemongo.bson.BSONObjectID

import scala.annotation.tailrec
import scala.math._

/**
 * Created by Szymek.edited by Ludwik
 */
 class SingleEliminationStrategy(val ListOfTeams:List[Team]) extends TournamentStrategy {

  //Generates full tree with NULLs
  override def generateTree(): EliminationTree = {

     val num = attachNumberOfTeams
    def log2(x: Double) = log(x) / log(2)
    //To get logaritm with base 2
    val deph = log2(num.toDouble).toInt
    //Recursion method to create tree with deph given in "deph"
    def addNull(root: Game, count : Int):Game = {
      if(count>0){
        root.left = new Game()
        root.right = new Game()
        root.value = null
        root.left = addNull(root.left,count-1)
        root.right = addNull(root.right,count-1)
        root
      }else
        root
    }
   new EliminationTree(addNull(new Game(),deph-1))
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

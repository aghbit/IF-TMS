package models.strategy.eliminationtrees

import models.strategy.strategies.DoubleEliminationStrategy
import models.strategy.{Match, EliminationTree}
import models.team.Team
import models.tournament.tournamenttype.TournamentType

import scala.StringBuilder
import scala.collection.mutable

/**
 * Created by Szymek Seget on 25.05.15.
 */
class DoubleEliminationTree(teams: List[Team],
                            tournamentType: TournamentType,
                            root: TreeNode) extends EliminationTree {

  val teamsNumber = teams.length

  val leafsNumber = DoubleEliminationStrategy.countLeaf(teamsNumber)

  val winnersTreeDepth = DoubleEliminationStrategy.countWinnerDepth(leafsNumber)

  val losersTreeDepth = DoubleEliminationStrategy.countLoserDepth(winnersTreeDepth)

  //sets ID for every match
  private var i = 0
  mapMatches(m => {
    m.id = i
    i=i+1
    m
  })
  //set parents for every node.
  setParentsDFS()

  /**
   * This method iterates through every tree node and set parent reference.
   * After that you can use this parent reference. It is useful while updating,
   * you can just take parent of node :)
   * Important! Parent of root (Final) is None.
   * @see Option, None
   */
  private def setParentsDFS(): Unit = {
    val stack = new mutable.Stack[TreeNode]()
    stack.push(root)
    while (stack.nonEmpty) {
      val tmp = stack.pop()
      tmp.right match {
        case Some(i) => {
          stack.push(i)
          i.parent=Some(tmp)
        }
        case None =>
      }
      tmp.left match {
        case Some(i) => {
          stack.push(i)
          i.parent=Some(tmp)
        }
        case None =>
      }
    }
  }


  /**
   * Simple BFS algorithm on elimination tree. Takes every match and changes it to f(match).
   * @param f
   */
  override def mapMatches(f:Match => Match) = {
    val queue = new mutable.Queue[TreeNode]()
    queue.enqueue(root)
    while(queue.nonEmpty) {
      val tmp = queue.dequeue()
      tmp.left match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      tmp.right match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      tmp.value = f(tmp.value)
    }
  }

  /**
   * Represents max depth of elimination tree. Counts from 0 (final), e.g.
   * For 32 teams depth equals 7.
   * For 16 teams depth equals 5
   */
  override def depth = (Math.log(teamsNumber)/Math.log(2)).asInstanceOf[Int] + 2

  override def getMatchesInNthRound(n:Int):List[Match] = {
    def helper(n:Int, root:Option[TreeNode]): List[Match] ={
      if(root.isEmpty) {
        List()
      }else if(n==0){
        List(root.get.value)
      }else {
        helper(n-1, root.get.left) ::: helper(n-1, root.get.right)
      }
    }
    helper(n, Some(root))
  }

  private def foreachTreeNodes(f:TreeNode => Unit) = {
    val queue = new mutable.Queue[TreeNode]()
    queue.enqueue(root)
    while(queue.nonEmpty) {
      val tmp = queue.dequeue()
      tmp.left match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      tmp.right match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      f(tmp)
    }
  }

  override def toString = {
    val builder = new StringBuilder()
    foreachTreeNodes(x => builder.append(x.toString))
    builder.result()
  }

  override def getNode(matchID: Int): TreeNode = ???

  override def setQFs(first: TreeNode, second: TreeNode, third: TreeNode, forth: TreeNode): Unit = {
    firstQF = first
    secondQF = second
    thirdQF = third
    forthQF = forth
  }

  override def addLoserToSecondQF(loser: Option[Team], prevMatchDepth:Int): Unit = {
    var places = getMatchesInNthRound(losersTreeDepth-(winnersTreeDepth-prevMatchDepth))
    while (!places.head.canAddTeam()){
      places = places.tail
    }
    places.head.addTeam(loser)
  }

  override def addLoserToThirdQF(loser: Option[Team], prevMatchDepth:Int): Unit = {
    var places = getMatchesInNthRound(losersTreeDepth-(winnersTreeDepth-prevMatchDepth)).reverse
    while (!places.head.canAddTeam()){
      places = places.tail
    }
    places.head.addTeam(loser)
  }
}

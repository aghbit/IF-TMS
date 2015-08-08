package models.strategy.eliminationtrees

import models.strategy.{Match, EliminationTree}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.{Json, JsObject}

import scala.collection.mutable

/**
 * Created by Szymek Seget on 08.08.15.
 */
class SingleEliminationTree (override val _id:ObjectId,
                            override val teamsNumber:Int,
                            override val tournamentType:TournamentType,
                            override val root:TreeNode) extends EliminationTree {

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

  /**
   * Simple BFS algorithm, but first takes right child, on elimination tree. Takes every match and changes it to f(match).
   * @param f
   */
  override def mapMatches(f:Match => Match) = {
    val queue = new mutable.Queue[TreeNode]()
    queue.enqueue(root)
    while(queue.nonEmpty) {
      val tmp = queue.dequeue()
      tmp.right match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      tmp.left match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      tmp.value = f(tmp.value)
    }
  }

  override def foreachTreeNodes(f:TreeNode => Unit) = {
    val queue = new mutable.Queue[TreeNode]()
    queue.enqueue(root)
    while(queue.nonEmpty) {
      val tmp = queue.dequeue()
      tmp.right match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      tmp.left match {
        case Some(i) => queue.enqueue(i)
        case None =>
      }
      f(tmp)
    }
  }

  /**
   * Represents max depth of elimination tree. Counts from 0 (final), e.g.
   * For 32 teams depth equals 7.
   * For 16 teams depth equals 5
   */
  override def depth = (Math.log(teamsNumber)/Math.log(2)).asInstanceOf[Int]

  override def getNode(matchID: Int): TreeNode = {
    val stack = new mutable.Stack[TreeNode]()
    stack.push(root)
    var loop = true
    var tmp:TreeNode = root
    while (stack.nonEmpty && loop) {
      tmp = stack.pop()
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

      if(tmp.value.id == matchID){
        loop = false
      }
    }
    tmp
  }

  override def iterator: Iterator[TreeNode] = {
    var list:List[TreeNode] = List()
    foreachTreeNodes(t => list=List(t) ::: list)
    list.iterator
  }

  override def toJson(): JsObject = {
    Json.obj(
      "losersTreeDepth" -> 0,
      "winnersTreeDepth" -> (depth+1),
      "match" -> root.value.toJson,
      "lefts" -> List(
        root.left.get.toJson("lefts"),
        root.right.get.toJson("lefts")
      )
    )
  }

}

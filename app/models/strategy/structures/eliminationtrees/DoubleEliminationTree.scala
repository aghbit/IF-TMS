package models.strategy.structures.eliminationtrees

import models.Participant
import models.strategy.strategies.DoubleEliminationStrategy
import models.strategy.structures.EliminationTree
import models.strategy.{EliminationStrategy, Match}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.{Json, JsObject}

import scala.collection.mutable

/**
 * Created by Szymek Seget on 25.05.15.
 */
class DoubleEliminationTree(override val _id:ObjectId,
                            override val teamsNumber:Int,
                            override val tournamentType: TournamentType,
                            override val root: TreeNode) extends EliminationTree {

  override val strategy: EliminationStrategy = DoubleEliminationStrategy

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
   * For 32 participants depth equals 7.
   * For 16 participants depth equals 5
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

  private def getNodesInNthRound(n:Int):List[TreeNode] = {
    def helper(n:Int, root:Option[TreeNode]): List[TreeNode] ={
      if(root.isEmpty) {
        List()
      }else if(n==0){
        List(root.get)
      }else {
        helper(n-1, root.get.left) ::: helper(n-1, root.get.right)
      }
    }
    helper(n, Some(root))
  }

  override def foreachNode(f:TreeNode => Unit) = {
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
    foreachNode(x => builder.append(x.toString))

    /*var i = losersTreeDepth
    builder.append("{rounds:[ ")
    while(i>=0){
      builder.append("{round: " + i + ", matches:[ ")
      getMatchesInNthRound(i).foreach(x => builder.append(x.toJson+","))
      builder.append("]}, ")
      i = i-1
    }
    builder.append("]}")*/

    builder.result()
  }

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

  def setQFs(first: TreeNode, second: TreeNode, third: TreeNode, forth: TreeNode): Unit = {
    firstQF = first
    secondQF = second
    thirdQF = third
    forthQF = forth
  }

  def addLoserToSecondQF(loser: Option[Participant], prevMatchDepth:Int): Unit = {
    var round = losersTreeDepth-(winnersTreeDepth-prevMatchDepth)*2+1
    if(prevMatchDepth == winnersTreeDepth){
      round = losersTreeDepth
    }
    var places = getMatchesInNthRound(round).reverse
    while (places.nonEmpty && !places.head.canAddParticipant()){
      places = places.tail
    }
    if(places.nonEmpty){
      places.head.addParticipant(loser)
    }
  }

  def addLoserToThirdQF(loser: Option[Participant], prevMatchDepth:Int): Unit = {
    var round = losersTreeDepth-(winnersTreeDepth-prevMatchDepth)*2+1
    if(prevMatchDepth == winnersTreeDepth){
      round = losersTreeDepth
    }
    var places = getMatchesInNthRound(round).reverse
    while (places.nonEmpty && !places.head.canAddParticipant()){
      places = places.tail
    }
    if(places.nonEmpty){
      places.head.addParticipant(loser)
    }
  }

  private def isInWinnerPart(m:Match): Boolean = {
    firstQF.contains(m) || forthQF.contains(m)
  }

  override def iterator: Iterator[TreeNode] = {
    var i = winnersTreeDepth
    var j = losersTreeDepth
    var list:List[TreeNode] = List()
    while(i>=1){
      val tmpWinners = getNodesInNthRound(i).filter(n => isInWinnerPart(n.value))
      list = list ::: tmpWinners

      val tmpLosers = getNodesInNthRound(j)
        .filter(n => !isInWinnerPart(n.value))
        .filter(n => !list.contains(n))

      list = list ::: tmpLosers
      if(j%2 == 0 && j!=losersTreeDepth) {
        val tmpLosers2 = getNodesInNthRound(j-1)
          .filter(n=> !isInWinnerPart(n.value))
          .filter(n => !list.contains(n))
        j=j-1
        list = list ::: tmpLosers2
      }
      i=i-1
      j=j-1
    }
    list.iterator
  }

  override def toJson(): JsObject = {
    Json.obj(
      "type" -> "DoubleEliminationTree",
      "discipline" -> tournamentType.getDisciplineName,
      "losersTreeDepth" -> losersTreeDepth,
      "winnersTreeDepth" -> winnersTreeDepth,
      "match" -> root.value.toJson,
      "lefts" -> List(
        Json.obj(
          "match"->root.left.get.value.toJson,
          "lefts" -> List(
            root.left.get.left.get.toJson("lefts"),
            root.right.get.right.get.toJson("lefts")
          )
        )
      ),
      "rights" -> List(
        Json.obj(
          "match"->root.right.get.value.toJson,
          "rights" -> List(
            root.left.get.right.get.toJson("rights"),
            root.right.get.left.get.toJson("rights")
          )
        )
      )
    )
  }

}
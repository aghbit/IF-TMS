package models.strategy.strategies



import models.strategy.Tree.{EliminationTree, Game}
import models.strategy.{Match, TournamentStrategy}
import models.team.Team

import scala.annotation.tailrec
import scala.math._
import scala.util.Random

/**
 * Created by Szymek.
 */
class DoubleEliminationStrategy (val listOfTeams:List[Team]) extends TournamentStrategy{
//  override var tree: EliminationTree = _
  var notYetPopulatedPlaces:Int = attachNumberOfTeams
  def populateTree(tree:EliminationTree, list: List[Team]): EliminationTree = {
    if(list.size<=4) throw new NotEnoughTeamsException(list.size+" teams is too few for DoubleEliminationStrategy")
    var left = tree.root.left.left
    var right = tree.root.right.left
    populateTree(left,populateTree(right,list))
    tree
  }

  protected def populateTree(root:Game,list:List[Team]):List[Team] = {
    if(root.left==null && root.right==null) {
      root.value = draw(list)
      list.filter(team => team._id!=root.value.host && team._id != root.value.guest)
    }
    else populateTree(root.right,populateTree(root.left,list))
  }

  protected def draw(list:List[Team]):Match = {
    var team1 = list(Random.nextInt(list.size))
    if(list.size<notYetPopulatedPlaces) {
      notYetPopulatedPlaces-=2
      Match(team1, null)
    }
    else {
      notYetPopulatedPlaces-=2
      var list2 = list.filter(team => team != team1)
      var team2 = list2(Random.nextInt(list2.size))
      Match(team1, team2)
    }
  }


  override def getNextMatch(): Match = ???

  def attachNumberOfTeams: Int = {
    @tailrec def compareAndReturn(n: Int): Int =
      if (n >= listOfTeams.size) n
      else compareAndReturn(2 * n)
    compareAndReturn(1)
  }

///  override def generateTree(): EliminationTree = ???


  override def generateTree(): EliminationTree = {

    val num = attachNumberOfTeams
    def log2(x: Double) = log(x) / log(2)
    //To get logaritm with base 2
    val deph = log2(num.toDouble).toInt+1
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

  override def updateTree(m: Match): EliminationTree = ???


/////////////////////////Methods useful in Testing/////////////////////////////////////
  def areAllTeamsSet(tree:EliminationTree, listOfTeams:List[Team]):Boolean = countTeams(tree.root,listOfTeams).isEmpty
  private def countTeams(root: Game, listOfTeams:List[Team]):List[Team] = {
    if(root.left==null && root.right==null) {
      root.value match {
        case null => listOfTeams
        case value => {
          tickTeams(listOfTeams,value)
        }
      }
    }
    else countTeams(root.left, countTeams(root.right,listOfTeams))
  }
  private def tickTeams(listOfTeams:List[Team], value:Match):List[Team] = {
    if(value.host!=null && !listOfTeams.exists(team => team._id == value.host)) throw new BadlyPopulatedTreeException
    if(value.guest!=null && !listOfTeams.exists(team => team._id == value.guest)) throw new BadlyPopulatedTreeException
    if(value.host==value.guest) throw new BadlyPopulatedTreeException
    listOfTeams.filter(team => team._id != value.host && team._id != value.guest)
  }

  def is2ndand4thQuarterEmpty(root:Game):Boolean = isQuarterEmpty(root.left.right) && isQuarterEmpty(root.right.right)

  private def isQuarterEmpty(root:Game):Boolean = {
    if(root.left==null || root.right==null)  root.value==null
    else isQuarterEmpty(root.left) && isQuarterEmpty(root.right)
  }
  //////////////////////////////////////////////////////////////////////////////
}

object DoubleEliminationStrategy{
  def apply(listOfTeams:List[Team]) = new DoubleEliminationStrategy(listOfTeams)
}
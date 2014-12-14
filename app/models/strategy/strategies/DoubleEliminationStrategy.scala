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


  var notYetPopulatedPlaces:Int = attachNumberOfTeams
  def populateTree(tree:EliminationTree, list: List[Team]): EliminationTree = {
    if(list.size<=4) throw new NotEnoughTeamsException(list.size+" teams is too few for DoubleEliminationStrategy")
    val left = tree.root.left.left
    val right = tree.root.right.left
    populateTree(left,populateTree(right,list))
    tree
  }

  private def populateTree(root:Game,list:List[Team]):List[Team] = {
    if(root.left==null && root.right==null) {
      root.value = draw(list)
      list.filter(team => team._id!=root.value.host && team._id != root.value.guest)
    }
    else populateTree(root.right,populateTree(root.left,list))
  }

  private def draw(list:List[Team]):Match = {
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

  def attachNumberOfTeams: Int = {
    @tailrec def compareAndReturn(n: Int): Int =
      if (n >= listOfTeams.size) n
      else compareAndReturn(2 * n)
    compareAndReturn(1)
  }

  override def generateTree(): EliminationTree = {

    val num = attachNumberOfTeams
    def log2(x: Double) = log(x) / log(2)
    //To get logaritm with base 2
    val deph = log2(num.toDouble).toInt+1
    //Recursion method to create tree with deph given in "deph"
    def addNull(root: Game,source:Game, count : Int):Game = {
      if(count>0){
        root.left = new Game()
        root.right = new Game()
        root.value = null
        root.parent = source
        root.left = addNull(root.left,root,count-1)
        root.right = addNull(root.right,root,count-1)
        root
      }else
        root
    }

    var tree = new EliminationTree()
    tree.root.left=new Game()
    tree.root.right=new Game()
    tree.root.left.parent=tree.root
    tree.root.right.parent=tree.root
    tree.root.left.left=new Game()
    tree.root.left.right=new Game()
    tree.root.right.left=new Game()
    tree.root.right.right=new Game()
    tree.root.left.left=addNull(tree.root.left.left,tree.root.left,deph-3)
    tree.root.left.right=addNull(tree.root.left.right,tree.root.left,deph-2)
    tree.root.right.left=addNull(tree.root.right.left,tree.root.right,deph-3)
    tree.root.right.right=addNull(tree.root.right.right,tree.root.right,deph-2)
    tree
//
//    new EliminationTree(addNull(new Game(),null,deph-1))
  }

  override def updateTree(tree: EliminationTree): EliminationTree = ???

//  def getAsscociatedWithWinner(game:Game, tree:EliminationTree):Game ={
//
//
//  }

  private def potentialQrtFinal(game:Game, tree:EliminationTree):Int = {  //1st and 3rd QF are winner branches, 2nd and 4th are looser branches
    def findQrtFinal(game:Game):Game={                            //returns 0 when it's semi-final or final
       if(getLevel(game)==2) game
       else findQrtFinal(game.parent)
    }
    if(getLevel(game)<=2) 0
    else{
      if(findQrtFinal(game)==tree.root.left.left) 1 else
      if(findQrtFinal(game)==tree.root.left.right) 2 else
      if(findQrtFinal(game)==tree.root.right.left) 3 else
      if(findQrtFinal(game)==tree.root.right.right) 4 else
      0
    }
  }



  private def getLevel(game:Game):Int={     // Final has 1st level, Semis have 2nd level and so on
    def getLevel(game:Game, count:Int): Int ={
      if(game.parent==null) count
      else getLevel(game.parent,count+1)
    }
    getLevel(game,0)+1
  }









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
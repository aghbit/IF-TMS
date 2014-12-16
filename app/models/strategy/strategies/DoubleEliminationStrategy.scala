package models.strategy.strategies



import models.strategy.Tree.{EliminationTree, Game}
import models.strategy.{Match, TournamentStrategy}
import models.team.Team

import scala.annotation.tailrec
import scala.collection.mutable
import scala.math._
import scala.util.Random

/**
 * Created by Szymek.
 */
class DoubleEliminationStrategy (val listOfTeams:List[Team]) extends TournamentStrategy{


  private var notYetPopulatedPlaces:Int = attachNumberOfTeams
  var maxLevel:Int = (log(attachNumberOfTeams.toDouble) / log(2)).toInt+1
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
    def addNull(root: Game, count : Int):Game = {
      if(count>0){
        root.left = new Game()
        root.right = new Game()
        root.value = null
        root.left = addNull(root.left, count - 1)
        root.left.parent = root
        root.right = addNull(root.right, count - 1)
        root.right.parent = root
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
    tree.root.left.left=addNull(tree.root.left.left,deph-3)
    tree.root.left.right=addNull(tree.root.left.right,2*deph-7)
    tree.root.right.left=addNull(tree.root.right.left,deph-3)
    tree.root.right.right=addNull(tree.root.right.right,2*deph-7)
    tree.root.left.left.parent=tree.root.left
    tree.root.left.right.parent=tree.root.left
    tree.root.right.left.parent=tree.root.right
    tree.root.right.right.parent=tree.root.right
    tree
//
//    new EliminationTree(addNull(new Game(),null,deph-1))
  }





  override def updateTree(tree: EliminationTree): EliminationTree = {
      var updatedTree = updateWinnerQuarter(tree.root.left.left,tree)
      updatedTree=updateLoserQuarter(tree.root.left.right,tree)
      updatedTree=updateWinnerQuarter(tree.root.right.left,tree)
      updatedTree=updateLoserQuarter(tree.root.right.right,tree)
      updatedTree=updateWinnerQuarterGame(tree.root.left,tree) // updating semis
      updatedTree
  }


  private def updateWinnerQuarter(game:Game,tree:EliminationTree):EliminationTree = {
    var updatedTree = updateWinnerQuarterGame(game,tree)
    if(game.left!=null) updatedTree=updateWinnerQuarter(game.left,updatedTree)
    if(game.right!=null) updatedTree=updateWinnerQuarter(game.right,updatedTree)
    updatedTree
  }

  private def updateWinnerQuarterGame(game:Game, tree:EliminationTree):EliminationTree = {
      if(game.value!=null) {
        if (game.value.isEnded) {
          val associatedMatch = getAssociatedWithWinner(game, tree) //winner of game
          if (associatedMatch.value != null) {
            if (associatedMatch.value.isEnded)
              if (game.parent.value == null)
                game.parent.value = new Match(game.value.winningTeam, associatedMatch.value.winningTeam)
          }
          if (getLevel(game) == maxLevel) {
            val associatedLoseMatch = getAssociatedWithLooser(game, tree) //loser of game
            if (associatedLoseMatch.value != null)
              if (associatedLoseMatch.value.isEnded)
                createMatchForLosersIn1stRound(game, tree)
          }
        }
      }
    tree
  }


  private def updateLoserQuarter(game:Game,tree:EliminationTree):EliminationTree = {
    var updatedTree = updateLoserQuarterGame(game,tree)
    if(game.left!=null) updatedTree=updateLoserQuarter(game.left,updatedTree)
    if(game.right!=null) updatedTree=updateLoserQuarter(game.right,updatedTree)
    updatedTree
  }

  private def updateLoserQuarterGame(game:Game,tree:EliminationTree):EliminationTree ={
    if(game.value!=null)
    if(game.value.isEnded){
      val associatedMatch = getAssociatedWithWinner(game,tree)
      if(associatedMatch.value!=null){
        if(associatedMatch.value.isEnded)
          if(game.parent.value==null)
            if(getLevel(game)%2==1)
              game.parent.value = new Match(associatedMatch.value.winningTeam,game.value.winningTeam)
            else
              game.parent.value = new Match(associatedMatch.value.losingTeam,game.value.winningTeam)
      }
    }
    tree
  }
///////////testing method///////////////
  def checkGameByRoute(route:String,game:Game):String={
    if(game.parent==null) route
    else if(game.parent.left==game) checkGameByRoute("l"+route,game.parent)
    else checkGameByRoute("r"+route,game.parent)
  }

  //useful in testing
  def getGame(root:Game,route:String):Game= {
    if (route.size==0) root
    else
      route.charAt(0) match{
        case 'r' => getGame(root.right,route.substring(1))
        case 'l' => getGame(root.left,route.substring(1))
        case _ => throw new BadParameterException
      }
  }


  private def createMatchForLosersIn1stRound(game:Game, tree:EliminationTree):Unit = {
      var stack = new mutable.Stack[Int]
      stack = goUpInWinnersQuarter(stack,game.parent)
      //viewStack(stack)

      def getMatchingQrtFinal(qrt:Int,tree:EliminationTree):Game=qrt match{
        case 1 => tree.root.right.right
        case 2 => tree.root.right.left
        case 3 => tree.root.left.right
        case 4 => tree.root.left.left
        case _ => null
      }

      var matchingQrtFinal = getMatchingQrtFinal(potentialQrtFinal(game,tree),tree)
      var nextGame= goDownInLosersQuarter(stack,matchingQrtFinal)


      if(nextGame.value==null) nextGame.value = new Match(game.value.losingTeam, getNeighbour(game).value.losingTeam)


  }

  private def viewStack(stack:mutable.Stack[Int]):Unit={
    if(!stack.isEmpty) {
      println("stack " + stack.pop())
      viewStack(stack)
    }
    else println("stack empty")
  }


  private def getAssociatedWithWinner(game:Game, tree:EliminationTree):Game ={
    if(getLevel(game)==1) throw new TournamentWinException
    val gameQrtFinal= potentialQrtFinal(game,tree)
    if(gameQrtFinal==0 || gameQrtFinal==1 || gameQrtFinal==3){
      getNeighbour(game)
    }
    else{
      if(getLevel(game) % 2==1) getNeighbour(game)
      else{
        var stack = new mutable.Stack[Int]      //0 - left 1 - right
        stack = goUp(stack,game)                //++ filling Stack
        if(getLevel(game)-maxLevel % 4 == 0)
          goDown(stack,getNeighbour(findQrtFinal(game).parent).left)       //right qrt-final in another half-tree
        else goDown(stack,findQrtFinal(game).parent.left)
      }
    }
  }
  private def getAssociatedWithLooser(game:Game, tree:EliminationTree):Game ={
    if(getLevel(game)==1) throw new LoserInFinalException
    val gameQrtFinal= potentialQrtFinal(game,tree)
    if(gameQrtFinal==0) throw new LoserInSemisException
    if(gameQrtFinal==2 || gameQrtFinal==4) throw new SecondLoseException

    if(getLevel(game)==maxLevel) getNeighbour(game)
    else throw new Exception
//      var stack = new mutable.Stack[Int]      //0 - left 1 - right


  }

  private def goUpInWinnersQuarter(stack:mutable.Stack[Int],game:Game):mutable.Stack[Int]={
      if(getLevel(game)==3) stack
      else{
        if(game.parent.left==game)
          stack.push(0)
        else stack.push(1)
        goUpInWinnersQuarter(stack,game.parent)
      }
  }
  private def goDownInLosersQuarter(stack:mutable.Stack[Int],game:Game):Game={
      var right = game.right
      if(stack.isEmpty) right
      else{
        if(stack.pop()==0) goDownInLosersQuarter(stack,right.left)
        else goDownInLosersQuarter(stack,right.right)
      }
  }

  private def goUp(stack:mutable.Stack[Int],game:Game):mutable.Stack[Int]={
    if(getLevel(game)==4) stack
    else {
      val parent = game.parent
      if (parent.parent.left == parent) stack.push(0)
      else stack.push(1)
      goUp(stack, parent.parent)
    }
  }
  private def goDown(stack:mutable.Stack[Int],game:Game):Game={
    if(stack.isEmpty) game
    else
    if(stack.pop()==0) goDown(stack,game.left)
    else goDown(stack,game.right)
  }
  def getNeighbour(game:Game):Game = {
    if(game.parent.left==game) game.parent.right
    else game.parent.left
  }
  private def potentialQrtFinal(game:Game, tree:EliminationTree):Int = {  //1st and 3rd QF are winner branches, 2nd and 4th are looser branches
    if(getLevel(game)<=2) 0
    else{
      if(findQrtFinal(game)==tree.root.left.left) 1 else
      if(findQrtFinal(game)==tree.root.left.right) 2 else
      if(findQrtFinal(game)==tree.root.right.left) 3 else
      if(findQrtFinal(game)==tree.root.right.right) 4 else
        0
    }
  }
  private def findQrtFinal(game:Game):Game={
    if(getLevel(game)==3) game
    else findQrtFinal(game.parent)
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
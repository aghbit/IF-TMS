package models.strategy.strategies



import models.strategy.Tree.{Game, EliminationTree}
import models.strategy.{Match, TournamentStrategy}
import models.team.Team

import scala.annotation.tailrec
import scala.collection.mutable
import scala.math._
import scala.util.Random

/**
 * Created by Szymek.
 */
class DoubleEliminationStrategy () extends TournamentStrategy{

  private var numberOfTeams:Int = 0
  private var notYetPopulatedPlaces:Int = 0
  private var maxLevel:Int = 0

  //  //populating only in 1st and 3rd quarter
  def populateTree(tree:EliminationTree, list: List[Team]): EliminationTree = {
    if(list.size<=4) throw new NotEnoughTeamsException(list.size+" teams is too few for DoubleEliminationStrategy")
    val left = tree.root.left.get.left
    val right = tree.root.right.get.left
    populateTree(left.get,populateTree(right.get,list))
    tree
  }
  //populating all the subtree with the given list
  private def populateTree(root:Game,list:List[Team]):List[Team] = {
    if(root.left==None && root.right==None) {
      root.value = draw(list)
      if(root.value.get.guest!=None)
        list.filter(team => team._id!=root.value.get.host.get && team._id != root.value.get.guest.get)
      else
        list.filter(team => team._id!=root.value.get.host.get)
    }
    else populateTree(root.right.get,populateTree(root.left.get,list))
  }
  //getting random teams to be playing against themselves
  private def draw(list:List[Team]):Option[Match] = {
    val team1 = list(Random.nextInt(list.size))
    if(list.size<notYetPopulatedPlaces) {
      notYetPopulatedPlaces-=2
      Some(Match(Some(team1), None))
    }
    else {
      notYetPopulatedPlaces-=2
      val list2 = list.filter(team => team != team1)
      val team2 = list2(Random.nextInt(list2.size))
      Some(Match(Some(team1), Some(team2)))
    }
  }
  //determines how many leaves to create
  private def attachNumberOfTeams(listOfTeams:List[Team]): Int = {
    @tailrec def compareAndReturn(n: Int): Int =
      if (n >= listOfTeams.size) n
      else compareAndReturn(2 * n)
    compareAndReturn(1)
  }
  // generating tree, 1st and 3rd quarter are regular size(just like in SingleEliminationStrategy)
  // 2nd and 4th are adjusted the size od 1st and 4th
  override def generateTree(listOfTeams:List[Team]): EliminationTree = {
    numberOfTeams = listOfTeams.size
    notYetPopulatedPlaces=attachNumberOfTeams(listOfTeams)
    maxLevel=(log(attachNumberOfTeams(listOfTeams).toDouble) / log(2)).toInt+1
    val num = attachNumberOfTeams(listOfTeams)
    def log2(x: Double) = log(x) / log(2)
    //To get logaritm with base 2
    val depth = log2(num.toDouble).toInt+1
    //Recursion method to create tree with depth given in "depth"
    def addNull(root: Game, count : Int):Game = {
      if(count>0) {
        root.left = Some(Game())
        root.right = Some(Game())
        root.value = None
        root.left = Some(addNull(root.left.get, count - 1))
        root.left.get.parent = Some(root)
        root.right = Some(addNull(root.right.get, count - 1))
        root.right.get.parent = Some(root)
      }
      root
    }

    val tree = new EliminationTree()
    tree.root.left=Some(Game())
    tree.root.right=Some(Game())
    tree.root.left.get.parent=Some(tree.root)
    tree.root.right.get.parent=Some(tree.root)
    tree.root.left.get.left=Some(Game())
    tree.root.left.get.right=Some(Game())
    tree.root.right.get.left=Some(Game())
    tree.root.right.get.right=Some(Game())
    tree.root.left.get.left=Some(addNull(tree.root.left.get.left.get,depth-3))
    tree.root.left.get.right=Some(addNull(tree.root.left.get.right.get,2*depth-7))
    tree.root.right.get.left=Some(addNull(tree.root.right.get.left.get,depth-3))
    tree.root.right.get.right=Some(addNull(tree.root.right.get.right.get,2*depth-7))
    tree.root.left.get.left.get.parent=tree.root.left
    tree.root.left.get.right.get.parent=tree.root.left
    tree.root.right.get.left.get.parent=tree.root.right
    tree.root.right.get.right.get.parent=tree.root.right
    tree
  }




  //getting across the tree and according to the score of every two associated matches creating new match
  override def updateTree(tree: EliminationTree): EliminationTree = {
    var updatedTree = updateWinnerQuarter(tree.root.left.get.left.get,tree)     //1st quarter
    updatedTree=updateLoserQuarter(tree.root.left.get.right.get,tree)           //2nd quarter
    updatedTree=updateWinnerQuarter(tree.root.right.get.left.get,tree)          //3rd quarter
    updatedTree=updateLoserQuarter(tree.root.right.get.right.get,tree)          //4th quarter
    updatedTree=updateWinnerQuarterGame(tree.root.left.get,tree)            //updating semis
    updatedTree
  }
  //
  // updating 1st and 3rd quarter
  private def updateWinnerQuarter(game:Game,tree:EliminationTree):EliminationTree = {
    var updatedTree = updateWinnerQuarterGame(game,tree)                            //updating game
    if(game.left!=None) updatedTree=updateWinnerQuarter(game.left.get,updatedTree)      //updating left tree
    if(game.right!=None) updatedTree=updateWinnerQuarter(game.right.get,updatedTree)    //updating right tree
    updatedTree
  }
  //updating match from winner quarter(updating winner teams and losers in 1st round,
  //losers in the next rounds will be updated as associated teams for winners in losers quarters)
  private def updateWinnerQuarterGame(game:Game, tree:EliminationTree):EliminationTree = {
    if(game.value!=None) {                                                           //match must exist
      if (game.value.get.isEnded) {                                                      //match must be finished
      val associatedMatch = getAssociatedWithWinner(game, tree)                    //get associated match for winner of game
        if (associatedMatch.value != None) {                                         //above mentioned match must exist
          if (associatedMatch.value.get.isEnded)                                         //and must be finished
            if (game.parent.get.value == None)
              game.parent.get.value = Some(new Match(game.value.get.winningTeam, associatedMatch.value.get.winningTeam)) //creating match for winners
        }
        if (getLevel(game) == maxLevel) {                                            //match is in the first round
        val associatedLoseMatch = getAssociatedWithLooser(game, tree)              //getting loser from associated first round
          if (associatedLoseMatch.value != None)                                     //associated match must exist
            if (associatedLoseMatch.value.get.isEnded)                                   //associated match must be finished
              createMatchForLosersIn1stRound(game, tree)                             //creating match in losers(in losers' guarter)
        }
      }
    }
    tree
  }
  //
  // updating 2nd and 4th quarter
  private def updateLoserQuarter(game:Game,tree:EliminationTree):EliminationTree = {
    var updatedTree = updateLoserQuarterGame(game,tree)                             //updating game
    if(game.left!=None) updatedTree=updateLoserQuarter(game.left.get,updatedTree)       //updating left tree
    if(game.right!=None) updatedTree=updateLoserQuarter(game.right.get,updatedTree)     //updating right tree
    updatedTree
  }
  //
  //updating match from losers' quarter(updating winner teams,
  //losers are dropped out from the tournament)
  private def updateLoserQuarterGame(game:Game,tree:EliminationTree):EliminationTree ={
    if(game.value!=None)                                                                //match must exist
      if(game.value.get.isEnded){                                                             //match must be finished
      val associatedMatch = getAssociatedWithWinner(game,tree)                          //get associated match for winner of game
        if(associatedMatch.value!=None){                                                  //above mentioned match must exist
          if(associatedMatch.value.get.isEnded)                                               //and must be finished
            if(game.parent.get.value==None)                                                   //we ensure that this pair wasn't earlier updated
              if(getLevel(game)%2==1)
                game.parent.get.value = Some(new Match(game.value.get.winningTeam,associatedMatch.value.get.winningTeam)) //creating match for winners in the same quarter
              else
                game.parent.get.value = Some(new Match(associatedMatch.value.get.losingTeam, game.value.get.winningTeam)) //creating match for this game and appropriate loser in winners' quarter

        }
      }
    tree
  }

  //loser and his neighbour's looser fall to the bottom of 2nd and 4th quarter
  private def createMatchForLosersIn1stRound(game:Game, tree:EliminationTree):Unit = {
    var stack = new mutable.Stack[Int]
    stack = goUpInWinnersQuarter(stack,game.parent.get)                                    //getting path from 1st round to quarterfinal
    // from 1st quarter they fall into the 4th quarter and from 3rd quarter to 2nd one
    // 1,2,3,4 - number of quarter
    def getMatchingQrtFinal(qrt:Int,tree:EliminationTree):Game=qrt match{
        case 1 => tree.root.right.get.right.get
        case 2 => tree.root.right.get.left.get
        case 3 => tree.root.left.get.right.get
        case 4 => tree.root.left.get.left.get
      }
    val matchingQrtFinal = getMatchingQrtFinal(potentialQrtFinal(game,tree),tree)
    val nextGame= goDownInLosersQuarter(stack,matchingQrtFinal)                       //going down according to the path hidden in stack
    //checking if this pair is updated and finally creating match for them
    if(nextGame.value==None) nextGame.value = Some(new Match(game.value.get.losingTeam, getNeighbour(game).value.get.losingTeam))
  }
  //
  //
  //If game is in 1st,3rd quarter or level of game in 2nd or 4th is odd - getting simply neighbour of game(next match then is their parent)
  //else getting appropriate loser from appropriate winners' quarter
  def getAssociatedWithWinner(game:Game, tree:EliminationTree):Game ={
    if(getLevel(game)==1) throw new TournamentWinException                //no match is associated with final. finalists only win or lose
    val gameQrtFinal= potentialQrtFinal(game,tree)                        //getting according qrtFinal for match
    if(gameQrtFinal==0 || gameQrtFinal==1 || gameQrtFinal==3) getNeighbour(game)     //0 - semi final                                             //getting simple neighbour
    else{
      if(getLevel(game) % 2==1) {

        getNeighbour(game) //if level is odd, also getting neighbour
      }
      else{
        var stack = new mutable.Stack[Int]                                //0 - left 1 - right
        stack = goUp(stack,game)                                          //filling Stack
        if((maxLevel-stack.size-3)%2==0)                             //once we search opponent from 1st quarter, once from 3rd one. to mix teams
          goDown(stack,getNeighbour(findQrtFinal(game).parent.get).left.get)      //searching in another half of tournament
        else goDown(stack,findQrtFinal(game).parent.get.left.get)                 //searching in the same half of tournament
      }
    }
  }
  //
  //  //should be invoked only if the team is in first round
  private def getAssociatedWithLooser(game:Game, tree:EliminationTree):Game ={
    if(getLevel(game)==1) throw new LoserInFinalException
    val gameQrtFinal= potentialQrtFinal(game,tree)
    if(gameQrtFinal==0) throw new LoserInSemisException
    if(gameQrtFinal==2 || gameQrtFinal==4) throw new SecondLoseException
    else getNeighbour(game)
  }
  //
  //in winners' quarter indicating route to quarterfinal, 0 - the game is left son of its' parent 1 - right
  private def goUpInWinnersQuarter(stack:mutable.Stack[Int],game:Game):mutable.Stack[Int]={
    if(getLevel(game)==3) stack
    else{
      if(game.parent.get.left.get==game)
        stack.push(0)
      else stack.push(1)
      goUpInWinnersQuarter(stack,game.parent.get)
    }
  }
  //
  //on the basis of stack from goUpInWinnersQuarter, going to according match
  private def goDownInLosersQuarter(stack:mutable.Stack[Int],game:Game):Game={
    val right = game.right                                                //the strategy consists in going right
    if(stack.isEmpty) right.get   //right
    else{
      if(stack.pop()==0) goDownInLosersQuarter(stack,right.get.left.get)          //and then choosing the route from stack
      else goDownInLosersQuarter(stack,right.get.right.get)
    }
  }
  //the same as goUpInWinnersQuarter but it goes up in losers' quarter
  private def goUp(stack:mutable.Stack[Int],game:Game):mutable.Stack[Int]={
    if(getLevel(game)==4) stack
    else {                                                                  //we leave the state where going right is obligatory
    val parent = game.parent.get
      if (parent.parent.get.left.get == parent) stack.push(0)                       //going left
      else stack.push(1)                                                    //going right
      goUp(stack, parent.parent.get)
    }
  }
  //the same as goDownInLosersQuarter but it goes down in winner's quarter
  //on the basis of goUp's stack
  private def goDown(stack:mutable.Stack[Int],game:Game):Game={
    if(stack.isEmpty) game
    else
    if(stack.pop()==0) goDown(stack,game.left.get)
    else goDown(stack,game.right.get)
  }
  //  //getting second son of its' parent
  def getNeighbour(game:Game):Game = {
    if(game.parent.get.right.get==game){
      game.parent.get.left.get}
    else
      game.parent.get.right.get
  }
  //  //getting qrtFinal as number
  private def potentialQrtFinal(game:Game, tree:EliminationTree):Int = {  //1st and 3rd QF are winner branches, 2nd and 4th are looser branches
    if(getLevel(game)<=2) 0
    else{
      if(findQrtFinal(game)==tree.root.left.get.left.get) 1 else
      if(findQrtFinal(game)==tree.root.left.get.right.get) 2 else
      if(findQrtFinal(game)==tree.root.right.get.left.get) 3 else
      if(findQrtFinal(game)==tree.root.right.get.right.get) 4 else
        0
    }}

  //  }
  //getting qrtFinal as Game object
  private def findQrtFinal(game:Game):Game={
    if(getLevel(game)==3) game
    else findQrtFinal(game.parent.get)
  }
  //getting on which level game is set on
  private def getLevel(game:Game):Int={     // Final has 1st level, Semis have 2nd level and so on
  def getLevel(game:Game, count:Int): Int ={
    if(game.parent==None) count
    else getLevel(game.parent.get,count+1)
  }
    getLevel(game,0)+1
  }


  /////////////////////////Methods useful in Testing/////////////////////////////////////

    def checkGameByRoute(route:String,game:Game):String={
      if(game.parent==None) route
      else if(game.parent.get.left.get==game) checkGameByRoute("l"+route,game.parent.get)
      else checkGameByRoute("r"+route,game.parent.get)
    }


  def getGame(root:Game,route:String):Game= {
    if (route.size==0) root
    else
      route.charAt(0) match{
        case 'r' => getGame(root.right.get,route.substring(1))
        case 'l' => getGame(root.left.get,route.substring(1))
        case _ => throw new BadParameterException
      }
  }

    def areAllTeamsSet(tree:EliminationTree, listOfTeams:List[Team]):Boolean = countTeams(tree.root,listOfTeams).isEmpty
    private def countTeams(root: Game, listOfTeams:List[Team]):List[Team] = {
      if(root.left==None && root.right==None) {
        root.value match {
          case None => listOfTeams
          case value => tickTeams(listOfTeams,value.get)
        }
      }
      else countTeams(root.left.get, countTeams(root.right.get,listOfTeams))
    }
    private def tickTeams(listOfTeams:List[Team], value:Match):List[Team] = {
      if(value.host!=None && !listOfTeams.exists(team => team._id == value.host.get)) throw new BadlyPopulatedTreeException
      if(value.guest!=None && !listOfTeams.exists(team => team._id == value.guest.get)) throw new BadlyPopulatedTreeException
      if(value.host==value.guest) throw new BadlyPopulatedTreeException
      if(value.host!=None && value.guest!=None)
        listOfTeams.filter(team => team._id != value.host.get && team._id != value.guest.get)
      else
        if(value.host!=None) listOfTeams.filter(team => team._id != value.host.get)
        else if(value.guest!=None) listOfTeams.filter(team => team._id != value.guest.get)
        else listOfTeams
    }

    def is2ndand4thQuarterEmpty(root:Game):Boolean = isQuarterEmpty(root.left.get.right.get) && isQuarterEmpty(root.right.get.right.get)

    private def isQuarterEmpty(root:Game):Boolean = {
      if(root.left==None || root.right==None)  root.value==None
      else isQuarterEmpty(root.left.get) && isQuarterEmpty(root.right.get)
    }
    //////////////////////////////////////////////////////////////////////////////

}

object DoubleEliminationStrategy{
  def apply() = new DoubleEliminationStrategy()
}
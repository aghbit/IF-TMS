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
  override def generateTree(listOfTeams:List[Team]): EliminationTree = {

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
        root.left.parent = root
        root.right = addNull(root.right,count-1)
        root.right.parent = root
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

  override def populateTree(tre: EliminationTree, list: List[Team]): EliminationTree = {
    var overrided_list = list
    var tree = tre
    if(list.isEmpty==true) {
      throw new NotEnoughTeamsException("Populating Tree failed. Empty list of teams.")}//no teams given
    def populate(root: Game) {
      //to use Game not ElminationTree
      if (overrided_list.isEmpty) {
      // System.out.println("Empty list")
        return 1 //End of populating

      }
        if (root.left != null)
        populate(root.left) //do recursion
      if (root.left==null) {
        if(root.value==null)
        root.value = Match(overrided_list.head, null) // Adding leaf
        else
        root.value = new Match(root.value.host,overrided_list.head._id)
        overrided_list = overrided_list.tail//removing first element from list
        //System.out.println("Removing object from list with lengh " + overrided_list.length)
      }
      if(root.parent!=null && root.parent.right!=root)
      populate(root.parent.right)
  } ;
    while(overrided_list.length>0) {
      populate(tree.root)
      System.out.println("--")
    }
    tree
  }

  override def updateTree(tree: EliminationTree): EliminationTree = {
    var root: Game = tree.root
    if(root==null) ()


    def check(tmp:Game): Unit ={
      if(tmp==null){
        //throw badTreeExcepttion
        null
      }
      if(tmp.value==null) {//have to go deeper
        check(tmp.left)
        check(tmp.right)
      }else {

        if(tmp.value.isEnded){
          if(tmp.parent.right!=tmp && tmp.parent.right.value.isEnded){
            //UPDAE!
            tmp.parent.value = new Match(tmp.value.winningTeam,tmp.parent.right.value.winningTeam)
           // System.out.println("Updating me! with "+tmp.parent.value.host.toString())
            null;//means updated
          }
        }
      }
      null//means not updated
    }
    check(root)
    new EliminationTree(root)

  }
  //Test method
  def getGame(root:Game,route:String):Game= {
    if (route.size==0) root
    else
      route.charAt(0) match{
        case 'r' => getGame(root.right,route.substring(1))
        case 'l' => getGame(root.left,route.substring(1))
        case _ => throw new BadParameterException
      }
  }
}
object SingleEliminationStrategy{
  def apply(listOfTeams:List[Team]) = new SingleEliminationStrategy(listOfTeams)
}
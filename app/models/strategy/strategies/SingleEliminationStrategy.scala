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
 class SingleEliminationStrategy() extends TournamentStrategy {

//  //Generates full tree with NULLs
//  override def generateTree(listOfTeams:List[Team]): EliminationTree = {
//     val num = attachNumberOfTeams(listOfTeams)
//    def log2(x: Double) = log(x) / log(2)
//    //To get logaritm with base 2
//    val deph = log2(num.toDouble).toInt
//    //Recursion method to create tree with deph given in "deph"
//    def addNull(root: Game, count : Int):Game = {
//      if(count>0){
//        root.left = new Game()
//        root.right = new Game()
//        root.value = null
//        root.left = addNull(root.left,count-1)
//        root.left.parent = root
//        root.right = addNull(root.right,count-1)
//        root.right.parent = root
//        root
//      }else
//        root
//    }
//   new EliminationTree(addNull(new Game(),deph-1))
//  }
//
//   def attachNumberOfTeams(listOfTeams:List[Team]): Int = {
//    @tailrec def compareAndReturn(n: Int): Int =
//      if (n >= listOfTeams.size) n
//      else compareAndReturn(2 * n)
//    compareAndReturn(1)
//  }
//// Fills the leafs with team
////for example for tree with 16 leafs and 20 teams
////after first "round" there will be only hosts filled
////after second round method will fill guest, (starting from left side) and
//// the last 4 leafs will have guest part filled with null (20-16 = 4)
//  override def populateTree(tre: EliminationTree, list: List[Team]): EliminationTree = {
//    var overrided_list = list
//    val tree = tre
//    if(list.isEmpty) {
//      throw new NotEnoughTeamsException("Populating Tree failed. Empty list of teams.")}//no teams given
//    def populate(root: Game) {
//      //to use Game not ElminationTree
//      if (overrided_list.isEmpty) {
//        return null//End of populating, its not in Scala way, but it  ends my method
//      }
//        if (root.left != null)
//        populate(root.left) //do recursion
//        if(root.left==null) {
//        if(root.value==null)
//        root.value = Match(overrided_list.head, null) // Adding leaf
//        else
//        root.value = new Match(root.value.host,overrided_list.head._id)
//        overrided_list = overrided_list.tail//removing first element from list
//      }
//      if(root.parent!=null && root.parent.right!=root)
//      populate(root.parent.right)
//  }
//    while(overrided_list.length>0) {
//      populate(tree.root)
//    }
//    tree
//  }
//
//  override def updateTree(tree: EliminationTree): EliminationTree = {
//    val root: Game = tree.root
//    def check(tmp:Game): Unit ={
//      if(tmp==null){
//        null
//      }
//      if(tmp.value==null) {//have to go deeper
//        check(tmp.left)
//        check(tmp.right)
//      }else {
//        if(tmp.value.isEnded){
//          if(tmp.parent.right!=tmp && tmp.parent.right.value.isEnded){
//            //UPDATE!
//            tmp.parent.value = new Match(tmp.value.winningTeam,tmp.parent.right.value.winningTeam)
//            null
//          }
//        }
//      }
//      null
//    }
//    check(root)
//    new EliminationTree(root)
//  }
//  //Test method
//  def getGame(root:Game,route:String):Game= {
//    if (route.size==0) root
//    else
//      route.charAt(0) match{
//        case 'r' => getGame(root.right,route.substring(1))
//        case 'l' => getGame(root.left,route.substring(1))
//        case _ => throw new BadParameterException
//      }
//  }


  override def generateTree(listOfTeams: List[Team]): EliminationTree = ???

  override def populateTree(tree: EliminationTree, listOfTeams: List[Team]): EliminationTree = ???

  override def updateTree(tree: EliminationTree): EliminationTree = ???


}
object SingleEliminationStrategy{
  def apply(listOfTeams:List[Team]) = new SingleEliminationStrategy()
}
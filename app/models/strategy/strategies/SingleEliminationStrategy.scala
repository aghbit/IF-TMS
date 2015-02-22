package models.strategy.strategies


import models.Game.{Game, EliminationTree}
import models.strategy.{TournamentStrategy, Match}
import models.team.Team

import scala.annotation.tailrec
import scala.math._

/**
 * Created by Szymek.edited by Ludwik
 */
 class SingleEliminationStrategy() extends TournamentStrategy {

  //Generates full tree with NULLs
  override def generateTree(listOfTeams:List[Team]): EliminationTree = {
     val num = attachNumberOfTeams(listOfTeams)
    def log2(x: Double) = log(x) / log(2)
    //To get logaritm with base 2
    val deph = log2(num.toDouble).toInt
    //Recursion method to create tree with deph given in "deph"
   new EliminationTree(new Game().addNull(deph-1))
  }

   def attachNumberOfTeams(listOfTeams:List[Team]): Int = {
    @tailrec def compareAndReturn(n: Int): Int =
      if (n >= listOfTeams.size) n
      else compareAndReturn(2 * n)
    compareAndReturn(1)
  }
  /*
  Fills the leafs with team
  for example for tree with 16 leafs and 20 teams
  after first "round" there will be only hosts filled
  after second round method will fill guest, (starting from left side) and
  the last 4 leafs will have guest part filled with null (20-16 = 4)
  */
  override def populateTree(tre: EliminationTree, list: List[Team]): EliminationTree = {
    var overrided_list = list
    val tree = tre
    if(list.isEmpty) {
      throw new NotEnoughTeamsException("Populating Tree failed. Empty list of teams.")}//no teams given
    def populate(root: Game) {
      //to use Game not ElminationTree
      if (overrided_list.isEmpty) {
        return None//End of populating, its not in Scala way, but it  ends my method
      }
        if (root.left != None)
        populate(root.left.get) //do recursion
        if(root.left==None) {
        if(root.value==None)
        root.value = Some(Match(Some(overrided_list.head), None)) // Adding leaf
        else
        root.value = Some(new Match(root.value.get.host,Some(overrided_list.head._id)))
        overrided_list = overrided_list.tail//removing first element from list
      }
      if(root.parent!=None && root.parent.get.right.get!=root)
      populate(root.parent.get.right.get)
  }
    while(overrided_list.length>0) {
      populate(tree.root)
    }
    tree
  }

  override def updateTree(tree: EliminationTree): EliminationTree = {
    val root: Game = tree.root
    def check(tmp:Option[Game]): Unit ={
      if(tmp==None){
        None
      }
      if(tmp.get.value==None) {//have to go deeper
        check(tmp.get.left)
        check(tmp.get.right)
      }else {
        if(tmp.get.value.get.isEnded){
          if(tmp.get.parent.get.right.get!=tmp.get && tmp.get.parent.get.right.get.value.get.isEnded){
            //UPDATE!
            tmp.get.parent.get.value = Some(new Match(tmp.get.value.get.winningTeam,tmp.get.parent.get.right.get.value.get.winningTeam))
            None
          }
        }
      }
      None
    }
    check(Some(root))
    new EliminationTree(root)
  }
  //Test method
  def getGame(root:Game,route:String):Game= {
    if (route.size==0) root
    else
      route.charAt(0) match{
        case 'r' => getGame(root.right.get,route.substring(1))
        case 'l' => getGame(root.left.get,route.substring(1))
        case _ => throw new IllegalArgumentException("You can go either to the right (r) or to the left (l)")
      }
  }




}
object SingleEliminationStrategy{
  def apply(listOfTeams:List[Team]) = new SingleEliminationStrategy()
}
package models.strategy

import models.strategy.Tree.EliminationTree
import models.team.Team

/**
 * Created by Szymek. Edited by Ludwik
 */
trait TournamentStrategy {
  var tree:EliminationTree;
  def generateTree():EliminationTree ={
    tree =new EliminationTree();
   tree;
  }
  def generateTree(count:Integer):EliminationTree;
  def populateTree(list:List[Team]);
  def updateTree(m:Match);
  def getNextMatch():Match;





}
/*
  val ListOfTeams: List[Team]
  val ListOfMatches: List[Match]
  val View: MatchStructure

  def draw[Option: String]: Unit = ???

  def setScore(id: Option[BSONObjectID], score: String)

  def getView(): MatchStructure

  def getOrder(): List[Team]

  def attachNumberOfTeams(): Int
  */


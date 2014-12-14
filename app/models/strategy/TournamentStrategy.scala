package models.strategy

import models.strategy.Tree.EliminationTree
import models.team.Team

/**
 * Created by Szymek. Edited by Ludwik
 */
trait TournamentStrategy {
  def generateTree():EliminationTree
  def populateTree(tree:EliminationTree,list:List[Team]):EliminationTree
  def updateTree(m:Match):EliminationTree
  def getNextMatch():Match
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


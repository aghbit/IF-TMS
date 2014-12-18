package models.strategy

import models.strategy.Tree.EliminationTree
import models.team.Team

/**
 * Created by Szymek. Edited by Ludwik
 */
trait TournamentStrategy {
  def generateTree(listOfTeams:List[Team]):EliminationTree
  def populateTree(tree:EliminationTree,listOfTeams:List[Team]):EliminationTree
  def updateTree(tree:EliminationTree):EliminationTree
}



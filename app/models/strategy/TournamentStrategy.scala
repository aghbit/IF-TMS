package models.strategy

import models.strategy.Tree.EliminationTree
import models.team.Team

/**
 * Created by Szymek. Edited by Ludwik
 */
trait TournamentStrategy {
  def generateTree():EliminationTree
  def populateTree(tree:EliminationTree,list:List[Team]):EliminationTree
  def updateTree(tree:EliminationTree):EliminationTree
}



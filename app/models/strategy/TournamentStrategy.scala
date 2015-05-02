package models.strategy

import models.Game.{Game, EliminationTree}
import models.team.Team

/**
 * Created by Szymek. Edited by Ludwik
 */
trait TournamentStrategy {
  def getGame(game: Game, s: String): Game

  def generateTree(listOfTeams: List[Team]): EliminationTree

  def drawTeamsInTournament(tree: EliminationTree, listOfTeams: List[Team]): EliminationTree

  def updateTree(tree: EliminationTree): EliminationTree
}



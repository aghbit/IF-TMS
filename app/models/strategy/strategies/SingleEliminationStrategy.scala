package models.strategy.strategies


import models.strategy.{EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.tournamenttype.TournamentType

/**
 * Created by Szymek
 */

object SingleEliminationStrategy extends EliminationStrategy{
  override def generateTree(teams: List[Team], tournamentType: TournamentType): EliminationTree = ???
}
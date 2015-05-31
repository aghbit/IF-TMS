package models.strategy.strategies


import models.strategy.{EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

/**
 * Created by Szymek
 */

object SingleEliminationStrategy extends EliminationStrategy{
  override def generateTree(teams: List[Team], tournamentType: TournamentType): EliminationTree = ???

  override def initEmptyTree(id:ObjectId, teamsNumber: Int, tournamentType: TournamentType): EliminationTree = ???
}
package models.strategy

import models.strategy.eliminationtrees.TreeNode
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId


/**
 * Created by Szymek
 */
trait EliminationStrategy {

  def generateTree(teams:List[Team], tournamentType: TournamentType):EliminationTree

  def initEmptyTree(id:ObjectId, teamsNumber: Int, tournamentType: TournamentType): EliminationTree

}



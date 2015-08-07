package models.strategy

import models.strategy.eliminationtrees.TreeNode
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId


/**
 * Created by Szymek
 */
trait EliminationStrategy {

  @throws(classOf[IllegalArgumentException])
  def generateTree(teams:List[Team], tournamentType: TournamentType, tournamentID:ObjectId):EliminationTree

  def initEmptyTree(id:ObjectId, teamsNumber: Int, tournamentType: TournamentType): EliminationTree

  def updateMatchResult(eliminationTree: EliminationTree, m:Match):EliminationTree
}



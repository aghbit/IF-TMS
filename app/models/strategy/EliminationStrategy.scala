package models.strategy

import models.strategy.structures.EliminationTree
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId


/**
 * Created by Szymek
 */
trait EliminationStrategy {

  @throws(classOf[IllegalArgumentException])
  def generate(teams:List[Team], tournamentType: TournamentType, tournamentID:ObjectId):EliminationStructure

  def initEmpty(id:ObjectId, teamsNumber: Int, tournamentType: TournamentType): EliminationStructure

  def updateMatchResult(eliminationStructure: EliminationStructure, m:Match):EliminationStructure
}



package models.strategy

import models.Participant

import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId


/**
 * Created by Szymek
 */
trait EliminationStrategy {

  @throws(classOf[IllegalArgumentException])
  def generate(teams:List[Participant], tournamentType: TournamentType, tournamentID:ObjectId):EliminationStructure

  def initEmpty(id:ObjectId, teamsNumber: Int, tournamentType: TournamentType): EliminationStructure

  def updateMatchResult(eliminationStructure: EliminationStructure, m:Match):EliminationStructure
}



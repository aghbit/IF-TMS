package models.strategy

import models.team.Team
import models.tournament.tournamenttype.TournamentType


/**
 * Created by Szymek
 */
trait EliminationStrategy {

  def generateTree(teams:List[Team], tournamentType: TournamentType):EliminationTree

}



package models.tournament.tournamenttype.tournamenttypes

import models.strategy.Score
import models.strategy.scores.BeachVolleyballScore
import models.tournament.tournamenttype.TournamentType

/**
 * Created by Szymek Seget on 25.05.15.
 */
object BeachVolleyball extends TournamentType{
  override def getNewScore(): Score = BeachVolleyballScore()
}

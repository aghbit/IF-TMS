package models.statistics.tournament.volleyballs

import models.statistics.tournament.TournamentStatistics

/**
 * Created by krzysiek.
 */
trait VolleyballsTournamentStatistics extends TournamentStatistics {
  val numberOfTieBreaks: Int

  def getNumberOfTieBreaks: Int = {
    numberOfTieBreaks
  }
}
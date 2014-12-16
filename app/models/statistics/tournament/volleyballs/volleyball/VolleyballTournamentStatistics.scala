package models.statistics.tournament.volleyballs.volleyball

import models.statistics.tournament.volleyballs.VolleyballsTournamentStatistics

/**
 * Created by krzysiek.
 */
class VolleyballTournamentStatistics(val discipline:String,
                                     val pointUnit:String,
                                     val numberOfMatches:Int,
                                     val numberOfSets:Int,
                                     val numberOfPoints:Int,
                                     val numberOfTieBreaks:Int,
                                     val numberOfSubstitutions:Int) extends VolleyballsTournamentStatistics {

  def getNumberOfSubstitutions:Int = {
    numberOfSubstitutions
  }

}

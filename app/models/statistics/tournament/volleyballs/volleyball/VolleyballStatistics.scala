package models.statistics.tournament.volleyballs.volleyball

import models.statistics.tournament.volleyballs.VolleyballsStatistics

/**
 * Created by krzysiek.
 */
class VolleyballStatistics (val discipline:String,
                            val pointUnit:String,
                            val numberOfMatches:Int,
                            val numberOfSets:Int,
                            val numberOfPoints:Int,
                            val numberOfTieBreaks:Int,
                            val numberOfSubstitutions:Int) extends VolleyballsStatistics {

  def getNumberOfSubstitutions:Int = {
    numberOfSubstitutions
  }

}

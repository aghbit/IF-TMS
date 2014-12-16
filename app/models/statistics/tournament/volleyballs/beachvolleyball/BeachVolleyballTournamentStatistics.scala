package models.statistics.tournament.volleyballs.beachvolleyball

import models.statistics.tournament.volleyballs.VolleyballsTournamentStatistics

/**
 * Created by krzysiek.
 */
class BeachVolleyballTournamentStatistics(val discipline:String,
                                          val pointUnit:String,
                                          val numberOfMatches:Int,
                                          val numberOfSets:Int,
                                          val numberOfPoints:Int,
                                          val numberOfTieBreaks:Int) extends VolleyballsTournamentStatistics {

}

package models.statistics.tournament.volleyballs.beachvolleyball

import models.statistics.tournament.volleyballs.VolleyballsStatistics

/**
 * Created by krzysiek.
 */
class BeachVolleyballStatistics (val discipline:String,
                                 val pointUnit:String,
                                 val numberOfMatches:Int,
                                 val numberOfSets:Int,
                                 val numberOfPoints:Int,
                                 val numberOfTieBreaks:Int) extends VolleyballsStatistics {

}

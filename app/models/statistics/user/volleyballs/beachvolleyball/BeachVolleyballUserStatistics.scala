package models.statistics.user.volleyballs.beachvolleyball

import models.statistics.user.TournamentsUserStatistics
import models.statistics.user.volleyballs.{MatchesAndSetsUserStatistics, VolleyballsUserStatistics}

/**
 * Created by krzysiek.
 */
class BeachVolleyballUserStatistics(val discipline:String,
                                    val pointUnit:String,
                                    var tournamentsStats:TournamentsUserStatistics,
                                    var matchesAndSetsStats: MatchesAndSetsUserStatistics,
                                    var winStreak:Int) extends VolleyballsUserStatistics {

}

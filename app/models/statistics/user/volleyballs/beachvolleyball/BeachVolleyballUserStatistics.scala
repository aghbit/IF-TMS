package models.statistics.user.volleyballs.beachvolleyball

import models.statistics.user.TournamentsUserStatistics
import models.statistics.user.volleyballs.{MatchesAndSetsUserStatistics, VolleyballsUserStatistics}
import org.bson.types.ObjectId

/**
 * Created by krzysiek.
 */
class BeachVolleyballUserStatistics(val _id: ObjectId,
                                    val discipline: String,
                                    val pointUnit: String,
                                    var tournamentsStats: TournamentsUserStatistics,
                                    var matchesAndSetsStats: MatchesAndSetsUserStatistics,
                                    var winStreak: Int) extends VolleyballsUserStatistics {

}

object BeachVolleyballUserStatistics {
  def apply(tournamentsStats: TournamentsUserStatistics): BeachVolleyballUserStatistics = {
    new BeachVolleyballUserStatistics(ObjectId.get(), "Beach Volleyball", "point", tournamentsStats,
      MatchesAndSetsUserStatistics("Beach Volleyball"), 0)
  }
}

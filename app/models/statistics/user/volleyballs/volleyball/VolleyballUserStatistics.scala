package models.statistics.user.volleyballs.volleyball

import models.statistics.user.TournamentsUserStatistics
import models.statistics.user.volleyballs.{MatchesAndSetsUserStatistics, VolleyballsUserStatistics}
import org.bson.types.ObjectId

/**
 * Created by krzysiek.
 */
class VolleyballUserStatistics(val _id: ObjectId,
                               val discipline: String,
                               val pointUnit: String,
                               var tournamentsStats: TournamentsUserStatistics,
                               var matchesAndSetsStats: MatchesAndSetsUserStatistics,
                               var winStreak: Int) extends VolleyballsUserStatistics {

}

object VolleyballUserStatistics {
  def apply(tournamentsStats: TournamentsUserStatistics): VolleyballUserStatistics = {
    new VolleyballUserStatistics(ObjectId.get(), "Volleyball", "point", tournamentsStats,
      MatchesAndSetsUserStatistics("Volleyball"), 0)
  }
}
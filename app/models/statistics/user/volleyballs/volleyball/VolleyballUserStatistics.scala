package models.statistics.user.volleyballs.volleyball

import models.statistics.user.TournamentsUserStatistics
import models.statistics.user.volleyballs.{MatchesAndSetsUserStatistics, VolleyballsUserStatistics}
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class VolleyballUserStatistics(val _id: BSONObjectID,
                               val discipline: String,
                               val pointUnit: String,
                               var tournamentsStats: TournamentsUserStatistics,
                               var matchesAndSetsStats: MatchesAndSetsUserStatistics,
                               var winStreak: Int) extends VolleyballsUserStatistics {

}

object VolleyballUserStatistics {
  def apply(tournamentsStats: TournamentsUserStatistics): VolleyballUserStatistics ={
    new VolleyballUserStatistics(BSONObjectID.generate,"Volleyball","point",tournamentsStats,
            MatchesAndSetsUserStatistics("Volleyball"),0)
  }
}
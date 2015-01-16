package models.statistics.user.volleyballs.beachvolleyball

import models.statistics.user.TournamentsUserStatistics
import models.statistics.user.volleyballs.{MatchesAndSetsUserStatistics, VolleyballsUserStatistics}
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class BeachVolleyballUserStatistics(val _id: BSONObjectID,
                                    val discipline: String,
                                    val pointUnit: String,
                                    var tournamentsStats: TournamentsUserStatistics,
                                    var matchesAndSetsStats: MatchesAndSetsUserStatistics,
                                    var winStreak: Int) extends VolleyballsUserStatistics {

}

object BeachVolleyballUserStatistics{
  def apply(tournamentsStats: TournamentsUserStatistics): BeachVolleyballUserStatistics = {
    new BeachVolleyballUserStatistics(BSONObjectID.generate,"Beach Volleyball","point",tournamentsStats,
            MatchesAndSetsUserStatistics("Beach Volleyball"),0)
  }
}

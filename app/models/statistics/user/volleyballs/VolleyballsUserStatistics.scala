package models.statistics.user.volleyballs

import models.statistics.user.UserStatistics

/**
 * Created by krzysiek.
 */
trait VolleyballsUserStatistics extends UserStatistics {
  var matchesAndSetsStats: MatchesAndSetsUserStatistics
}
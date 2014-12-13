package models.statistics.user.volleyballs.volleyball

import models.statistics.user.volleyballs.{MatchesAndSetsStatistics, VolleyballsStatistics}
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class VolleyballStatistics(val discipline:String,
                           val pointUnit:String,
                           var tournamentsID:Array[BSONObjectID],
                           var numberOfTournaments:Int,
                           var numberOfWonTournaments:Int,
                           var MatchesAndSetsStats: MatchesAndSetsStatistics,
                           var numberOfPoints:Int,
                           var winStreak:Int) extends VolleyballsStatistics {

}

package models.statistics.tournament.volleyballs.beachvolleyball

import models.statistics.tournament.volleyballs.VolleyballsTournamentStatistics
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class BeachVolleyballTournamentStatistics(val _id: BSONObjectID,
                                          val discipline: String,
                                          val pointUnit: String,
                                          val numberOfMatches: Int,
                                          val numberOfSets: Int,
                                          val numberOfPoints: Int,
                                          val numberOfTieBreaks: Int) extends VolleyballsTournamentStatistics {

}

object BeachVolleyballTournamentStatistics {
  def apply(): BeachVolleyballTournamentStatistics = {
    new BeachVolleyballTournamentStatistics(BSONObjectID.generate, "Beach Volleyball", "point", 0, 0, 0, 0)
  }
}
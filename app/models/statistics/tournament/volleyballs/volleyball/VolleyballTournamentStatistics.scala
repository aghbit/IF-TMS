package models.statistics.tournament.volleyballs.volleyball

import models.statistics.tournament.volleyballs.VolleyballsTournamentStatistics
import org.bson.types.ObjectId

/**
 * Created by krzysiek.
 */
class VolleyballTournamentStatistics(val _id: ObjectId,
                                     val discipline: String,
                                     val pointUnit: String,
                                     val numberOfMatches: Int,
                                     val numberOfSets: Int,
                                     val numberOfPoints: Int,
                                     val numberOfTieBreaks: Int,
                                     val numberOfSubstitutions: Int) extends VolleyballsTournamentStatistics {

  def getNumberOfSubstitutions: Int = {
    numberOfSubstitutions
  }

}

object VolleyballTournamentStatistics {
  def apply(): VolleyballTournamentStatistics = {
    new VolleyballTournamentStatistics(ObjectId.get(), "Volleyball", "point", 0, 0, 0, 0, 0)
  }
}
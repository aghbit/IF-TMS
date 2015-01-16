package models.statistics.tournament.volleyballs.volleyball

import models.statistics.tournament.volleyballs.VolleyballsTournamentStatistics
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class VolleyballTournamentStatistics(val _id: BSONObjectID,
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

object VolleyballTournamentStatistics{
  def apply(): VolleyballTournamentStatistics ={
    new VolleyballTournamentStatistics(BSONObjectID.generate,"Volleyball","point",0,0,0,0,0)
  }
}
package models.statistics.user.volleyballs.volleyball

import models.exceptions.{NegativeValueException, TooManySetsInMatchException}
import models.statistics.user.volleyballs.VolleyballsStatistics
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class VolleyballStatistics(var discipline:String,
                           var pointUnit:String,
                           var tournamentsID:Array[BSONObjectID],
                           var numberOfTournaments:Int,
                           var numberOfWonTournaments:Int,
                           var numberOfWonMatches:Int,
                           var numberOfLostMatches:Int,
                           var numberOfWonSets:Int,
                           var numberOfLostSets:Int,
                           var numberOfPoints:Int,
                           var winStreak:Int) extends VolleyballsStatistics {

  override def addNumberOfWonSets(sets:Int):Unit = {
    if(sets > 5){
      throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 5).")
    }
    else if(sets < 0){
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    numberOfWonSets += sets
  }

  override def addNumberOfLostSets(sets:Int):Unit = {
    if(sets > 5){
      throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 5).")
    }
    else if(sets < 0){
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    numberOfLostSets += sets
  }
}

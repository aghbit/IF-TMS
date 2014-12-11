package models.statistics.volleyball.beachvolleyball

import models.exceptions.TooManySetsInMatchException
import models.statistics.volleyball.VolleyballStatistics

/**
 * Created by krzysiek.
 */
trait BeachVolleyballStats extends VolleyballStatistics{
  val discipline:String
  val pointUnit:String
  var numberOfMatches:Int
  var numberOfSets:Int
  var numberOfPoints:Int

  override def addNumberOfSets(sets:Int): Unit ={
    if(sets > 3){
      throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 3).")
    }
    numberOfSets += sets
  }

}

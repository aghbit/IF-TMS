package models.statistics.volleyball.volleyball

import models.exceptions.TooManySetsInMatchException
import models.statistics.volleyball.VolleyballStatistics

/**
 * Created by krzysiek.
 */
trait VolleyballStats extends VolleyballStatistics{
  val discipline:String
  val pointUnit:String
  var numberOfMatches:Int
  var numberOfSets:Int
  var numberOfPoints:Int

  override def addNumberOfSets(sets:Int): Unit ={
    if(sets > 5){
      throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 5).")
    }
    numberOfSets += sets
  }
}

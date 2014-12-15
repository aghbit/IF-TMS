package models.statistics.user.volleyballs

import models.exceptions.{NegativeValueException, TooManySetsInMatchException}

/**
 * Created by krzysiek.
 */
class MatchesAndSetsStatistics(val discipline:String,
                               var numberOfWonMatches:Int,
                               var numberOfLostMatches:Int,
                               var numberOfWonSets:Int,
                               var numberOfLostSets:Int){

  def addNumberOfWonMatches():Unit = {
    numberOfWonMatches += 1
  }

  def addNumberOfLostMatches():Unit = {
    numberOfLostMatches += 1
  }

  def validityCheck(sets:Int):Boolean = {
    if(sets < 0){
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    if(discipline == "Beach Volleyball" && sets > 3){
        throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 3).")
    }
    else if(discipline == "Volleyball" && sets > 5){
        throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 5).")
    }
    true
  }

  def addNumberOfWonSets(sets:Int):Unit = {
    if(validityCheck(sets)) {
      numberOfWonSets += sets
    }
  }

  def addNumberOfLostSets(sets:Int):Unit = {
    if(validityCheck(sets)) {
      numberOfLostSets += sets
    }
  }
}

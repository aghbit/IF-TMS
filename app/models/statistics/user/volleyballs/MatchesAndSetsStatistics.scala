package models.statistics.user.volleyballs

import models.exceptions.{NegativeValueException, TooManySetsInMatchException}

/**
 * Created by krzysiek.
 */
class MatchesAndSetsStatistics(var numberOfWonMatches:Int,
                               var numberOfLostMatches:Int,
                               var numberOfWonSets:Int,
                               var numberOfLostSets:Int){

  def addNumberOfWonMatches():Unit = {
    numberOfWonMatches += 1
  }

  def addNumberOfLostMatches():Unit = {
    numberOfLostMatches += 1
  }

  def addNumberOfWonSets(sets:Int):Unit = {
    if(sets > 3){
      throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 3).")
    }
    else if(sets < 0){
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    numberOfWonSets += sets
  }

  def addNumberOfLostSets(sets:Int):Unit = {
    if(sets > 3){
      throw new TooManySetsInMatchException("Can't add. Too many sets in this match (Max 3).")
    }
    else if(sets < 0){
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    numberOfLostSets += sets
  }
}

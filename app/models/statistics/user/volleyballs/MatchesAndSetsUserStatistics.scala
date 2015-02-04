package models.statistics.user.volleyballs

import models.exceptions.{NegativeValueException, TooManySetsInMatchException}

/**
 * Created by krzysiek.
 */
class MatchesAndSetsUserStatistics(val discipline: String,
                                   var numberOfWonMatches: Int,
                                   var numberOfLostMatches: Int,
                                   var numberOfWonSets: Int,
                                   var numberOfLostSets: Int,
                                   var numberOfPoints: Int) {

  def addNumberOfWonMatches(): Unit = {
    numberOfWonMatches += 1
  }

  def addNumberOfLostMatches(): Unit = {
    numberOfLostMatches += 1
  }

  def validityCheck(sets: Int): Boolean = {
    if (sets < 0) {
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    if (discipline == "Beach Volleyball" && sets > 2) {
      throw new TooManySetsInMatchException("Can't add. Too many won/lost sets in this match (Max 2).")
    }
    else if (discipline == "Volleyball" && sets > 3) {
      throw new TooManySetsInMatchException("Can't add. Too many won/lost sets in this match (Max 3).")
    }
    true
  }

  def addNumberOfWonSets(sets: Int): Unit = {
    if (validityCheck(sets)) {
      numberOfWonSets += sets
    }
  }

  def addNumberOfLostSets(sets: Int): Unit = {
    if (validityCheck(sets)) {
      numberOfLostSets += sets
    }
  }

  def addNumberOfPoints(points: Int): Unit = {
    if (points < 0) {
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    numberOfPoints += points
  }
}


object MatchesAndSetsUserStatistics{
  def apply(discipline:String): MatchesAndSetsUserStatistics ={
    new MatchesAndSetsUserStatistics(discipline,0,0,0,0,0)
  }
}
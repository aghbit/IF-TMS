package models.statistics.user.volleyballs

import models.exceptions.NegativeValueException
import models.statistics.user.UserStatistics

/**
 * Created by krzysiek.
 */
trait VolleyballsStatistics extends UserStatistics {
  var numberOfWonSets:Int
  var numberOfLostSets:Int
  var numberOfPoints:Int

  def addNumberOfWonSets(sets:Int)
  def getNumberOfWonSets:Int = {
    numberOfWonSets
  }

  def addNumberOfLostSets(sets:Int)
  def getNumberOfLostSets:Int = {
    numberOfLostSets
  }

  def addNumberOfPoints(points:Int):Unit = {
    if(points<0){
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    numberOfPoints += points
  }
  def getNumberOfPoints:Int = {
    numberOfPoints
  }

}
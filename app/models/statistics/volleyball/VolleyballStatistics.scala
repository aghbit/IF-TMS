package models.statistics.volleyball

import models.statistics.Statistics

/**
 * Created by krzysiek.
 */
trait VolleyballStatistics extends Statistics {
  val discipline:String
  val pointUnit:String
  var numberOfMatches:Int
  var numberOfSets:Int
  var numberOfPoints:Int

  def addNumberOfMatches(matches:Int):Unit = {
    numberOfMatches += matches
  }
  def getNumberOfMatches:Int = {
    numberOfMatches
  }

  def addNumberOfSets(sets:Int)
  def getNumberOfSets:Int = {
    numberOfSets
  }

  def addNumberOfPoints(points:Int):Unit = {
    numberOfPoints += points
  }
  def getNumberOfPoints:Int = {
    numberOfPoints
  }
}

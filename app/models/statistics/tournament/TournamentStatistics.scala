package models.statistics.tournament

import models.statistics.Statistics

/**
 * Created by krzysiek.
 */
trait TournamentStatistics extends Statistics {
  val numberOfMatches: Int
  val numberOfSets: Int
  val numberOfPoints: Int

  def getNumberOfMatches: Int = {
    numberOfMatches
  }

  def getNumberOfSets: Int = {
    numberOfSets
  }

  def getNumberOfPoints: Int = {
    numberOfPoints
  }
}

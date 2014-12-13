package models.statistics.user

import models.statistics.Statistics
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
trait UserStatistics extends Statistics{
  var tournamentsID:Array[BSONObjectID]
  var numberOfTournaments:Int
  var numberOfWonTournaments:Int
  var numberOfWonMatches:Int
  var numberOfLostMatches:Int
  var winStreak:Int

  def addTournaments(tournament:BSONObjectID):Unit = {
    tournamentsID :+= tournament
    numberOfTournaments = tournamentsID.length
  }
  def getTournaments:Array[BSONObjectID] = {
    tournamentsID
  }

  def getNumberOfTournaments:Int = {
    numberOfTournaments
  }

  def addNumberOfWonTournaments():Unit = {
    numberOfWonTournaments += 1
  }
  def getNumberOfWonTournaments:Int = {
    numberOfWonTournaments
  }

  def addNumberOfWonMatches():Unit = {
    numberOfWonMatches += 1
  }
  def getNumberOfWonMatches:Int = {
    numberOfWonMatches
  }

  def addNumberOfLostMatches():Unit = {
    numberOfLostMatches += 1
  }
  def getNumberOfLostMatches:Int = {
    numberOfLostMatches
  }

  def didTeamWin(win:Boolean):Unit = { // true - win, false - loss
    if(win) winStreak+=1 else winStreak=0
  }
  def getWinStreak:Int = {
    winStreak
  }

}

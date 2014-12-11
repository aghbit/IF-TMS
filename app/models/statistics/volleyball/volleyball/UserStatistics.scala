package models.statistics.volleyball.volleyball

import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class UserStatistics(val discipline:String,
                     val pointUnit:String,
                     var numberOfTournaments:Int,
                     var numberOfMatches:Int,
                     var numberOfSets:Int,
                     var numberOfPoints:Int) extends VolleyballStats{
  var numberOfWonTournaments:Int = 0
  var numberOfWonMatches:Int = 0
  var winStreak:Int = 0
  var tournaments:Array[BSONObjectID] = Array() // tournamentID

  def addTournaments(tournament:BSONObjectID):Unit = {
    tournaments :+= tournament
  }
  def getTournaments:Array[BSONObjectID] = {
    tournaments
  }

  def updateNumberOfTournaments():Unit = {
    numberOfTournaments = tournaments.length
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

  def didTeamWin(win:Boolean):Unit = { // true - win, false - loss
    if(win) winStreak+=1 else winStreak=0
  }
  def getWinStreak:Int = {
    winStreak
  }

}

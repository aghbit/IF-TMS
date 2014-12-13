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
  var winStreak:Int

  def addTournament(tournament:BSONObjectID):Unit = {
    tournamentsID :+= tournament
    numberOfTournaments = tournamentsID.length
  }

  def addNumberOfWonTournaments():Unit = {
    numberOfWonTournaments += 1
  }

  def didTeamWin(win:Boolean):Unit = { // true - win, false - loss
    if(win) winStreak+=1 else winStreak=0
  }
}

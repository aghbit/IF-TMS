package models.statistics.user

import models.statistics.Statistics

/**
 * Created by krzysiek.
 */
trait UserStatistics extends Statistics{
  var tournamentsStats:TournamentsUserStatistics
  var winStreak:Int

  def didTeamWin(win:Boolean):Unit = { // true - win, false - loss
    if(win) winStreak+=1 else winStreak=0
  }
}

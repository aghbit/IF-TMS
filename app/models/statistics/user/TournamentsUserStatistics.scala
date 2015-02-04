package models.statistics.user

import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
class TournamentsUserStatistics(var tournamentsID: Array[BSONObjectID],
                                var numberOfTournaments: Int,
                                var numberOfWonTournaments: Int) {

  def addTournament(tournament: BSONObjectID): Unit = {
    tournamentsID :+= tournament
    numberOfTournaments = tournamentsID.length
  }

  def addNumberOfWonTournaments(): Unit = {
    numberOfWonTournaments += 1
  }

}

object TournamentsUserStatistics {
  def apply(tournamentsID: Array[BSONObjectID]): TournamentsUserStatistics ={
    new TournamentsUserStatistics(tournamentsID,0,0)
  }
}
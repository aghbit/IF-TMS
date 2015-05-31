package models.statistics.user

import org.bson.types.ObjectId


/**
 * Created by krzysiek.
 */
class TournamentsUserStatistics(var tournamentsID: Array[ObjectId],
                                var numberOfTournaments: Int,
                                var numberOfWonTournaments: Int) {

  def addTournament(tournament: ObjectId): Unit = {
    tournamentsID :+= tournament
    numberOfTournaments = tournamentsID.length
  }

  def addNumberOfWonTournaments(): Unit = {
    numberOfWonTournaments += 1
  }

}

object TournamentsUserStatistics {
  def apply(tournamentsID: Array[ObjectId]): TournamentsUserStatistics = {
    new TournamentsUserStatistics(tournamentsID, 0, 0)
  }
}
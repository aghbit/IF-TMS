package models.tournament.tournamentstate

import org.joda.time.{DateTime, Duration, Interval}

/**
 * Created by Szymek.
 */
class TournamentTerm(var enrollDeadline: DateTime,
                     var begin: DateTime,
                     var end: DateTime,
                     var extraBegin: DateTime,
                     var extraEnd: DateTime) {

  def getDuration: Duration = {
    val tournamentDuration = new Interval(begin, end).toDuration
    tournamentDuration
  }

}

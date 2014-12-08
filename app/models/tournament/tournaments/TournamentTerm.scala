package models.tournament.tournaments

import org.joda.time.{Interval, DateTime, Duration}

/**
 * Created by Szymek.
 */
class TournamentTerm(var enrollDeadline: DateTime,
                      var begin: DateTime,
                      var end: DateTime,
                      var extraBegin: DateTime,
                      var extraEnd:DateTime) {

  def getDuration() = {
    val duration = new Interval(begin, end).toDuration
    duration
  }

}

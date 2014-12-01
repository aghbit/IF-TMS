package models.tournament

import org.joda.time.DateTime

/**
 * Created by Szymek.
 */
class TournamentTerm(val enrollDeadline: DateTime,
                      val begin: DateTime,
                      val end:DateTime,
                      val extraBegin: DateTime,
                      val extraEnd:DateTime) {

  def getDuration() = ???

}

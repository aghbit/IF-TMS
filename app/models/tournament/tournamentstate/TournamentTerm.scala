package models.tournament.tournamentstate

import org.joda.time.{DateTime, Duration, Interval}
import play.api.libs.json.{Reads, Writes, Json}

/**
 * Created by Szymek.
 */
case class TournamentTerm(var enrollDeadline: DateTime,
                     var begin: DateTime,
                     var end: DateTime,
                     var extraBegin: DateTime,
                     var extraEnd: DateTime) {

  private def checkEnrollmentBeforeBegin() = {
    enrollDeadline.isBefore(begin)
  }

  private def checkDate(): Boolean = {
    begin.isBefore(end)
  }

  private def checkExtraDate(): Boolean = {
    extraBegin.isBefore(extraEnd)
  }

  private def checkBeginDateBeforeExtraDate(): Boolean = {
    begin.isBefore(extraBegin)
  }

  def isValid: Boolean = {
    checkEnrollmentBeforeBegin() && checkDate() && checkExtraDate() && checkBeginDateBeforeExtraDate()
  }


  def getDuration: Duration = {
    val tournamentDuration = new Interval(begin, end).toDuration
    tournamentDuration
  }

}
object JsonFormatTournamentTerm {
  implicit val yourJodaDateReads = Reads.jodaDateReads("yyyy-MM-dd' 'HH:mm:ss")
  implicit val yourJodaDateWrites = Writes.jodaDateWrites("yyyy-MM-dd' 'HH:mm:ss")
  implicit val tournamentTermFormat = Json.format[TournamentTerm]
}

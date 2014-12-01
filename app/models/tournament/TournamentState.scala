package models.tournament

/**
 * Created by Szymek.
 */
object TournamentState extends Enumeration{
  type State = Value
  val BeforeEnrollment, Enrollment, Break, BeforeTournament, Tournament, AfterTournament = Value

}

case class TournamentState(enum: TournamentState)
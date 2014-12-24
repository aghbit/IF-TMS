package models.tournament.tournamentstate

/**
 * Created by Przemek.
 */
object TournamentDiscipline extends Enumeration{
  type Discipline = Value
  val BeachVolleyball, Volleyball = Value

}

case class TournamentDiscipline(enum: TournamentDiscipline)


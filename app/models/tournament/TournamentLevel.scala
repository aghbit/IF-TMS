package models.tournament

/**
 * Created by Szymek.
 */
object TournamentLevel extends Enumeration{
  type Level = Value
  val Beginner, Intermediate, Pro = Value

}

case class TournamentLevel(enum: TournamentLevel)


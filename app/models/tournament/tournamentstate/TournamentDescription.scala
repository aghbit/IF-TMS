package models.tournament.tournamentstate

import play.api.libs.json.Json

/**
 * Created by Szymek..
 */
case class TournamentDescription(var name: String,
                            var place: String,
                            var description: String) {

}
object JsonFormatTournamentDescription {
  implicit val tournamentDescriptionFormat = Json.format[TournamentDescription]
}
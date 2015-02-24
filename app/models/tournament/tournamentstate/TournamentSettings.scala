package models.tournament.tournamentstate

import play.api.libs.json.Json

/**
 * Created by Przemek.
 */

case class TournamentSettings(var numberOfPitches: Int,
                         var numberOfTeams: Int,
                         var canEnroll: Boolean,
                         var level: Int,
                         var discipline: String) {
  // as suggested
}
object JsonFormatTournamentSettings {
  implicit val tournamentSettingsFormat = Json.format[TournamentSettings]
}
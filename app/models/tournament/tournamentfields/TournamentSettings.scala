package models.tournament.tournamentfields

import play.api.libs.json.Json

/**
 * Created by Przemek.
 */

case class TournamentSettings(var numberOfPitches: Int,
                         var numberOfTeams: Int,
                         var canEnroll: Boolean,
                         var level: Int,
                         var discipline: String,
                         var isEnrollmentEnded:Boolean) {

  def isValid = numberOfTeams>1 && numberOfPitches>0

  // as suggested
}
object JsonFormatTournamentSettings {
  implicit val tournamentSettingsFormat = Json.format[TournamentSettings]
}
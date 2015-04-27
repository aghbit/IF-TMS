package models.tournament.tournamentfields

import play.api.libs.json.Json
import models.tournament.tournamentfields.JsonFormatTournamentDescription._
import models.tournament.tournamentfields.JsonFormatTournamentSettings._
import models.tournament.tournamentfields.JsonFormatTournamentTerm._

/**
 * Edited by: Przemek
 */
case class TournamentProperties(var description: TournamentDescription,
                           var term: TournamentTerm,
                           var settings: TournamentSettings) {

}
object JsonFormatTournamentProperties {
  implicit val tournamentPropertiesFormat = Json.format[TournamentProperties]
}


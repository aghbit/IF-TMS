package models.tournament.tournamentstate

import play.api.libs.json.Json
import models.tournament.tournamentstate.JsonFormatTournamentDescription._
import models.tournament.tournamentstate.JsonFormatTournamentSettings._
import models.tournament.tournamentstate.JsonFormatTournamentTerm._

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


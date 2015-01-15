package models.tournament.tournamentstate

import models.strategy.TournamentStrategy

/**
 * Edited by: Przemek
 */
class TournamentProperties(var description: TournamentDescription,
                           var term: TournamentTerm,
                           var settings: TournamentSettings,
                           val strategy: TournamentStrategy,
                           val staff: TournamentStaff) {

}


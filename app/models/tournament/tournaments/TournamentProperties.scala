package models.tournament.tournaments

import models.strategy.TournamentStrategy

/**
 * Created by Szymek.
 * Edited by: Przemek
 */
class TournamentProperties(var description: TournamentDescription,
                            var term: TournamentTerm,
                            var settings: TournamentSettings,
                            val strategy: TournamentStrategy,
                            val staff: TournamentStaff) {

}


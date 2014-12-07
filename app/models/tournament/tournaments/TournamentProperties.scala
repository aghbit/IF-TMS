package models.tournament.tournaments

import models.strategy.TournamentStrategy

/**
 * Created by Szymek.
 * Edited by: Przemek
 */
class TournamentProperties(val description: TournamentDescription,
                            val term: TournamentTerm,
                            val settings: TournamentSettings,
                            val strategy: TournamentStrategy,
                            var state: TournamentState.Value,
                            val staff: TournamentStaff) {

}

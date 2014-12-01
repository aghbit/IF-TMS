package models.tournament

import models.strategies.TournamentStrategy

/**
 * Created by Szymek.
 */
class TournamentProperties(val description: TournamentDescription,
                            val term: TournamentTerm,
                            val settings: TournamentSettings,
                            val strategy: TournamentStrategy,
                            val staff: TournamentStaff) {

}

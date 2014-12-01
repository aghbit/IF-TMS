package models.tournament

/**
 * Created by Szymek.
 */

import TournamentLevel._

class TournamentSettings(val numberOfPitches: Int,
                          val numberOfTeams: Int,
                          val canEnroll: Boolean,
                          val level: TournamentLevel) {

}

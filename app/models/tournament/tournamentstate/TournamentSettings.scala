package models.tournament.tournamentstate

/**
 * Created by Szymek.
 */

class TournamentSettings(var numberOfPitches: Int,
                          var numberOfTeams: Int,
                          var canEnroll: Boolean,
                          var level: TournamentLevel.Value) {
}

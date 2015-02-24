package repositories

import models.tournament.tournaments.Tournament

/**
 * Created by Szymek.
 */
class TournamentRepository extends Repository[Tournament] {

  override val collectionName: String = "Tournaments"
  override val clazz: Class[Tournament] = classOf[Tournament]

}

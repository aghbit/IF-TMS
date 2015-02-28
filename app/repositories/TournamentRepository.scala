package repositories

import models.tournament.tournaments.Tournament

/**
 * Created by Szymek.
 */
class TournamentRepository extends Repository[Tournament] {

  override val collectionName: String = "Tournaments"
  override val clazz: Class[Tournament] = classOf[Tournament]

  def insert(tournament: Tournament) = {
    if(!tournament.isReadyToSave){
      throw new IllegalArgumentException("Tournament is not ready to save!")
    }else{
      mongoTemplate.save(tournament, collectionName)
    }
  }

}

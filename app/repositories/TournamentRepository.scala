package repositories

import models.tournament.Tournament

/**
 * Created by Szymek.
 */
class TournamentRepository extends Repository[Tournament] {

  override val collectionName: String = "Tournaments"
  override val clazz: Class[Tournament] = classOf[Tournament]

  @throws[IllegalArgumentException]
  def insert(tournament: Tournament) = {
    if(!tournament.isReadyToSave){
      throw new IllegalArgumentException("Tournament is not ready to save (Probably contains illegal values)!")
    }else{
      mongoTemplate.save(tournament, collectionName)
    }
  }

}

package repositories

import models.strategy.TournamentStrategy
import models.tournament.Tournament


/**
 * Created by ludwik on 07.05.15.
 */
class TournamentStrategyRepository extends Repository[TournamentStrategy]{
  override val collectionName: String = "Strategies"
  override val clazz: Class[TournamentStrategy] = classOf[TournamentStrategy]

  @throws[IllegalArgumentException]
  def insert(strategy: TournamentStrategy) = {
    if(!strategy.isReadyToSave){
      throw new IllegalArgumentException("Strategy is not ready to save (Probably contains illegal values)!")
    }else{
      mongoTemplate.save(strategy, collectionName)
    }
  }
}

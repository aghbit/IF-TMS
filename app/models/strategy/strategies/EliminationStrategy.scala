package models.strategy.strategies

import models.strategy.TournamentStrategy


/**
 * Created by Rafal on 2014-12-02.
 */
trait EliminationStrategy extends TournamentStrategy{
  val isThirdPlaceMatch:Boolean
  val isSeeding:Boolean
  def attachNumberOfTeams:Int
}

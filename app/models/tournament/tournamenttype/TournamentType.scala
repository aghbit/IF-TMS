package models.tournament.tournamenttype

import models.strategy.Score

/**
 * Created by Szymek Seget on 25.05.15.
 */
trait TournamentType {

  def getNewScore():Score

}

package models.tournament.tournamenttype

import models.strategy.Score
import models.team.Team
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 25.05.15.
 */
trait TournamentType {

  def getNewScore():Score
  def getNewTeam(name: String):Team
  def getNewTeam(id:ObjectId, name: String):Team

}

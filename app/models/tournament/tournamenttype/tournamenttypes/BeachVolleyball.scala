package models.tournament.tournamenttype.tournamenttypes

import models.strategy.Score
import models.strategy.scores.BeachVolleyballScore
import models.team.Team
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 25.05.15.
 */
object BeachVolleyball extends TournamentType{
  override def getNewScore(): Score = BeachVolleyballScore()

  override def getNewTeam(name: String): Team = BeachVolleyballTeam(name)

  override def getNewTeam(id: ObjectId, name: String): Team = new BeachVolleyballTeam(id, name)
}

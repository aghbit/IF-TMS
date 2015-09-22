package models.tournament.tournamenttype.tournamenttypes

import models.strategy.Score
import models.strategy.scores.VolleyballScore
import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 25.05.15.
 */
object Volleyball extends TournamentType{

  override def getNewScore(): Score = VolleyballScore()

  override def getNewParticipant(name: String): Team = VolleyballTeam(name)

  override def getNewParticipant(id: ObjectId, name: String): Team = new VolleyballTeam(id, name)
}

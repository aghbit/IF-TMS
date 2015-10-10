package models.tournament.tournamenttype.tournamenttypes

import models.strategy.ParticipantType.ParticipantType
import models.strategy.{ParticipantType, Score}
import models.team.Team
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 25.05.15.
 */
object BeachVolleyball extends TournamentType{
  override def getNewScore(): Score = new Score(3)

  override def getNewParticipant(name: String): Team = BeachVolleyballTeam(name)

  override def getNewParticipant(id: ObjectId, name: String): Team = new BeachVolleyballTeam(id, name)

  override def getParticipantType: ParticipantType = ParticipantType.TEAM
}

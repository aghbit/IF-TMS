package models.tournament.tournamenttype.tournamenttypes

import models.strategy.Score
import models.strategy.scores.SpeedmintonScore
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 13.09.15.
 */
object Speedminton extends TournamentType {

  override def getNewScore(): Score = SpeedmintonScore()

  override def getNewParticipant(name: String): Team = ???

  override def getNewParticipant(id: ObjectId, name: String): Team = ???
}

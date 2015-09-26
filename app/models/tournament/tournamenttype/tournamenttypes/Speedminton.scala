package models.tournament.tournamenttype.tournamenttypes

import models.Participant
import models.player.players.SpeedmintonPlayer
import models.strategy.ParticipantType.ParticipantType
import models.strategy.ParticipantType.ParticipantType
import models.strategy.{ParticipantType, Score}
import models.strategy.scores.SpeedmintonScore
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 13.09.15.
 */
object Speedminton extends TournamentType {

  override def getNewScore(): Score = SpeedmintonScore()

  override def getNewParticipant(nickName: String): Participant = SpeedmintonPlayer(nickName)

  override def getNewParticipant(id: ObjectId, nickName: String): Participant = SpeedmintonPlayer(id, nickName)

  override def getParticipantType: ParticipantType = ParticipantType.PLAYER
}

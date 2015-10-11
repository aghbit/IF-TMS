package models.tournament.tournamenttype.tournamenttypes

import models.Participant
import models.player.players.SpeedmintonPlayer
import models.strategy.ParticipantType.ParticipantType
import models.strategy.scores.newscores.PointsContainer
import models.strategy.scores.newscores.speedminton.SpeedmintonSet
import models.strategy.{ParticipantType, Score}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

/**
 * Created by Szymek Seget on 13.09.15.
 */
object Speedminton extends TournamentType {

  override def getNewScore(): Score = new Score(3)

  override def getNewParticipant(nickName: String): Participant = SpeedmintonPlayer(nickName)

  override def getNewParticipant(id: ObjectId, nickName: String): Participant = SpeedmintonPlayer(id, nickName)

  override def getParticipantType: ParticipantType = ParticipantType.PLAYER

  override def getNewPointsContainer(s: String, hostScore:JsValue, guestScore:JsValue): PointsContainer = {
    val hostPoints = hostScore.validate[String].get.toInt
    val guestPoints = guestScore.validate[String].get.toInt
    s match {
      case "SpeedmintonSet" => new SpeedmintonSet(hostPoints, guestPoints)
      case _ => throw new Exception("Wrong value " + s)
    }
  }

  override def getDisciplineName: String = "Speedminton"
}

package models.tournament.tournamenttype

import models.Participant
import models.strategy.ParticipantType.ParticipantType
import models.strategy.Score
import models.strategy.scores.newscores.PointsContainer
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

/**
 * Created by Szymek Seget on 25.05.15.
 */
trait TournamentType {

  def getNewScore():Score
  def getNewParticipant(nickName: String):Participant
  def getNewParticipant(id:ObjectId, nickName: String):Participant
  def getParticipantType:ParticipantType
  def getNewPointsContainer(s:String, hostScore:JsValue, guestScore:JsValue):PointsContainer
  def getDisciplineName:String

}

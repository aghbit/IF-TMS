package models.tournament.tournamenttype.tournamenttypes

import models.strategy.ParticipantType.ParticipantType
import models.strategy.scores.newscores.PointsContainer
import models.strategy.scores.newscores.beachvolleyball.{BeachVolleyballTieBreak, BeachVolleyballSet}
import models.strategy.{ParticipantType, Score}
import models.team.Team
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

/**
 * Created by Szymek Seget on 25.05.15.
 */
object BeachVolleyball extends TournamentType{
  override def getNewScore(): Score = new Score(3)

  override def getNewParticipant(name: String): Team = BeachVolleyballTeam(name)

  override def getNewParticipant(id: ObjectId, name: String): Team = new BeachVolleyballTeam(id, name)

  override def getParticipantType: ParticipantType = ParticipantType.TEAM

  override def getNewPointsContainer(s: String, hostScore:JsValue, guestScore:JsValue): PointsContainer = {
    val hostPoints = hostScore.validate[String].get.toInt
    val guestPoints = guestScore.validate[String].get.toInt
    s match {
      case "BeachVolleyballSet" => new BeachVolleyballSet(hostPoints, guestPoints)
      case "BeachVolleyballTieBreak" => new BeachVolleyballTieBreak(hostPoints, guestPoints)
      case _ => throw new Exception("Wrong value " + s)
    }
  }

  override def getDisciplineName: String = "BeachVolleyball"
}

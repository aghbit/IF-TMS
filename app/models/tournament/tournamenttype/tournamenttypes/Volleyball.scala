package models.tournament.tournamenttype.tournamenttypes

import models.strategy.ParticipantType.ParticipantType
import models.strategy.scores.newscores.PointsContainer
import models.strategy.scores.newscores.volleyball.{VolleyballSet, VolleyballTieBreak}
import models.strategy.{ParticipantType, Score}
import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

/**
 * Created by Szymek Seget on 25.05.15.
 */
object Volleyball extends TournamentType{

  override def getNewScore(): Score = new Score(5)

  override def getNewParticipant(name: String): Team = VolleyballTeam(name)

  override def getNewParticipant(id: ObjectId, name: String): Team = new VolleyballTeam(id, name)

  override def getParticipantType: ParticipantType = ParticipantType.TEAM

  override def getNewPointsContainer(s: String, hostScore:JsValue, guestScore:JsValue): PointsContainer = {
    val hostPoints = hostScore.validate[String].get.toInt
    val guestPoints = guestScore.validate[String].get.toInt
    s match {
      case "VolleyballSet" => new VolleyballSet(hostPoints, guestPoints)
      case "VolleyballTieBreak" => new VolleyballTieBreak(hostPoints, guestPoints)
      case _ => throw new Exception("Wrong value " + s)
    }
  }

  override def getDisciplineName: String = "Volleyball"
}

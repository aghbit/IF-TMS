package models.strategy

import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait TournamentStrategy {

  val ListOfTeams: List[BSONObjectID]
  var matches:Map[Match,Int]

  def draw:Unit

  def setScore(game: Match, score: Score): Unit

  def getOrder(): List[Team]

  def attachNumberOfTeams(): Int
}

package models.strategy

import models.strategy.matches.Match
import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait TournamentStrategy {

  val ListOfTeams: List[Team]
  val ListOfMatches: List[Match]
  val View: MatchStructure

  def draw[Option: String]: Unit = ???

  def setScore(id: Option[BSONObjectID], score: String)

  def getView(): MatchStructure

  def getOrder(): List[Team]

  def attachNumberOfTeams(): Int
}

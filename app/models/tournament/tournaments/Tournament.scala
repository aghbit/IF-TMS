package models.tournament.tournaments

import models.team.Team
import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class Tournament(val _id: Option[BSONObjectID],
                  val properties: TournamentProperties,
                  val state: TournamentState,
                  val teams: List[Team],
                  val users: List[User]) {

  def generateTree() = ???

  def startBeforeEnrollment() = ???

  def startEnrollment() = ???

  def startBreak() = ???

  def startBeforeTournament() = ???

  def startTournament() = ???

  def startAfterTournament() = ???



}

package models.tournament

import models.teams.Team
import models.users.User
import reactivemongo.bson.BSONObjectID

import TournamentState._

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

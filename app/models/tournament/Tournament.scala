package models.tournament.tournaments

import models.team.Team
import models.tournament.tournamentstate._
import models.user.User
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by: Przemek
 */


trait Tournament {
  val _id: BSONObjectID
  var properties: TournamentProperties
  var teams: ListBuffer[BSONObjectID]

  def startNext(): Tournament

  def addTeam(team: Team): Unit

  def removeTeam(team: Team): Unit

  def editTerm(term: TournamentTerm): Unit

  def generateTree() = {
   // properties.strategy.getOrder()
  }

  def addReferee(user: User): Unit = {
    properties.staff.addReferee(user)
  }

  def removeReferee(user: User): Unit = {
    properties.staff.removeReferee(user)
  }

  def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
    this.properties.settings.numberOfTeams = settings.numberOfTeams
  }

  def editDescription(description: TournamentDescription): Unit = {
    this.properties.description.description = description.description
  }

  def containsTeam(team: Team): Boolean = {
    teams.contains(team._id)
  }

  def containsReferee(referee: User): Boolean = {
    properties.staff.contains(referee)
  }
}
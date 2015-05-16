package models.tournament

import java.util

import models.strategy.TournamentStrategy
import models.team.Team
import models.tournament.tournamentfields.JsonFormatTournamentProperties._
import models.tournament.tournamentfields._
import models.user.User
import play.api.libs.json.{JsArray, JsObject, Json}
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by: Przemek
 */


trait Tournament {

  val _id: BSONObjectID
  var properties: TournamentProperties
  var teams: util.ArrayList[BSONObjectID]
  val strategy: TournamentStrategy
  val staff: TournamentStaff

  def startNext(): Tournament

  def addTeam(team: Team): Unit

  def removeTeam(team: Team): Unit

  def editTerm(term: TournamentTerm): Unit

  def generateTree(teams: List[Team]) = {
    strategy.generateTree(teams)
  }

  def addReferee(user: User): Unit = {
    staff.addReferee(user)
  }

  def removeReferee(user: User): Unit = {
    staff.removeReferee(user)
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
    staff.contains(referee)
  }

  def isReadyToSave = properties.settings.isValid && properties.term.isValid

  def getTeams = teams

  def toJson = {
    val tournamentPropertiesJson = Json.toJson(properties)
//    tournamentPropertiesJson.as[JsObject].+("_id", Json.toJson(_id.stringify))
    Json.obj(
      "_id"->_id.stringify,
      "properties"->tournamentPropertiesJson,
      "staff"->staff.toJson
    )
  }
}

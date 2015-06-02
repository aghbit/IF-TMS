package models.tournament

import java.util

import models.strategy.{EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.tournamentfields.JsonFormatTournamentProperties._
import models.tournament.tournamentfields._
import models.tournament.tournamenttype.TournamentType
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import models.user.User
import org.bson.types.ObjectId
import play.api.libs.json.{JsObject, Json}
import assets.ObjectIdFormat._
import play.api.libs.json.{JsArray, JsObject, Json}
import assets.ObjectIdFormat._
import scala.collection.mutable.ListBuffer

/**
 * Created by: Przemek
 */


trait Tournament {

  val _id: ObjectId
  var properties: TournamentProperties
  var teams: util.ArrayList[ObjectId]
  var tree: EliminationTree = _
  val staff: TournamentStaff
  var strategy:EliminationStrategy

  def startNext(): Tournament

  def addTeam(team: Team): Unit

  def removeTeam(team: Team): Unit

  def editTerm(term: TournamentTerm): Unit

  def generateTree(teams: List[Team]) = {
    tree = strategy.generateTree(teams, BeachVolleyball, _id)
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
    Json.obj(
      "_id"->_id,
      "properties"->tournamentPropertiesJson,
      "staff"->staff.toJson,
      "class"->this.getClass.toString
    )
  }
}

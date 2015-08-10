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
import scala.collection.JavaConversions._

/**
 * Created by: Przemek
 */


trait Tournament {


  val _id: ObjectId
  var properties: TournamentProperties
  var teams: util.ArrayList[Team]
  val staff: TournamentStaff
  var strategy:EliminationStrategy
  val discipline:TournamentType

  def startNext(): Tournament

  def addTeam(team: Team): Unit

  def removeTeam(team: Team): Unit

  def editTerm(term: TournamentTerm): Unit

  @throws(classOf[IllegalArgumentException])
  def generateTree():EliminationTree  = {
    try{
      strategy.generateTree(teams.toList, BeachVolleyball, _id)
    }catch {
      case e:IllegalArgumentException => throw new IllegalArgumentException(e)
    }
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
    teams.contains(team)
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

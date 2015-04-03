package controllers

import models.exceptions.TooManyMembersInTeamException
import models.player.players.{DefaultPlayerImpl, Captain}
import models.team.teams.volleyball.volleyballs.{TeamObject, BeachVolleyballTeam}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
import repositories.{PlayerRepository, TeamRepository, TournamentRepository}
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import scala.reflect.runtime.universe
import scala.collection.JavaConversions._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

import scala.concurrent.Future
import scala.util.matching.Regex

/**
 * Created by Szymek Seget on 07.03.15.
 */
object TeamsController extends Controller {

  val tournamentRepository = new TournamentRepository()
  val teamRepository = new TeamRepository()
  val playerRepository = new PlayerRepository()

  def createTeam(id:String) = Action.async(parse.json){
    request =>
      //Pull data from request.
      val teamName = request.body.\("teamName").validate[String](minLength[String](5))
      val captainName = request.body.\("captainName").validate[String](minLength[String](5)).get
      val captainSurname = request.body.\("captainSurname").validate[String](minLength[String](5)).get
      val captainPhone =request.body.\("captainPhone").validate[String](pattern(new Regex("^[0-9]+$"))).get
      val captainMail = request.body.\("captainMail").validate[String](email).get
      //Create captain
      val captain = Captain(captainName, captainSurname, captainPhone, captainMail)
      //Find tournament to check discipline
      val query = new Query(Criteria where "_id" is BSONObjectID(id))
      val tournament = tournamentRepository.find(query).get(0)
      //Create right Team Class.
      val teamClass = "models.team.teams.volleyball.volleyballs."+
                       tournament.properties.settings.discipline + "Team$"
      val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
      val module = runtimeMirror.staticModule(teamClass)
      val obj = runtimeMirror.reflectModule(module).instance.asInstanceOf[TeamObject]
      val team = obj(teamName.get)
      //Add captain
      team.addPlayer(captain)
      team.setCaptain(captain)
      tournament.addTeam(team)
      //Insert team & captain to DB.
      teamRepository.insert(team)
      playerRepository.insert(captain)
      tournamentRepository.insert(tournament)
      Future.successful(Ok(Json.obj("id"->team._id.stringify)))
  }





  def getTeam(id: String) = Action.async {
    val query = new Query(Criteria where "_id" is BSONObjectID(id))
    val team = teamRepository.find(query).get(0)
    Future.successful(Ok(team.toJson))
  }

  def getTeams(id: String) = Action.async {
    request =>
      val query = new Query(Criteria where "_id" is BSONObjectID(id))
      val tournament = tournamentRepository.find(query).get(0)
      val teamsIDs = tournament.getTeams
      val teams = teamsIDs.map(teamID => {
        val query = new Query(Criteria where "_id" is teamID)
        teamRepository.find(query).get(0)
      })
      val teamsJSON = teams.map(team => team.toJson)
      Future.successful(Ok(Json.toJson(teamsJSON)))
  }
}

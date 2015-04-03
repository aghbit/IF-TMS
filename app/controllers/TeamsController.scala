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


import scala.concurrent.Future

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
      val teamName = request.body.\("teamName").validate[String].get
      val captainName = request.body.\("captainName").validate[String].get
      val captainSurname = request.body.\("captainSurname").validate[String].get
      val captainPhone =request.body.\("captainPhone").validate[String].get
      val captainMail = request.body.\("captainMail").validate[String].get
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
      val team = obj(teamName)
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


  def createPlayer(teamId: String) = Action.async(parse.json) {
    request =>
      val query = new Query(Criteria where "_id" is BSONObjectID(teamId))
      val team = teamRepository.find(query).get(0)
      val name = request.body.\("name").validate[String].get
      val surname = request.body.\("surname").validate[String].get
      val player = DefaultPlayerImpl(name, surname)
      try {
        team.addPlayer(player)
        playerRepository.insert(player)
        teamRepository.insert(team)
        Future.successful(Ok("Player added!"))
      }catch {
        case e:TooManyMembersInTeamException => Future.failed(e)
        case e:Throwable => Future.failed(e)
      }
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

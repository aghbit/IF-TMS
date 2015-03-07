package controllers

import controllers.helpers.RequestHelper
import models.player.players.{DefaultPlayerImpl, Captain}
import models.team.teams.volleyball.volleyballs.{TeamObject, BeachVolleyballTeam}
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
import repositories.{PlayerRepository, TeamRepository, TournamentRepository}
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import scala.reflect.runtime.universe


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
      val teamName = RequestHelper.fromJsonRequestToString(request, "teamName")
      val captainName = RequestHelper.fromJsonRequestToString(request, "captainName")
      val captainSurname = RequestHelper.fromJsonRequestToString(request, "captainSurname")
      val captainPhone = RequestHelper.fromJsonRequestToString(request, "captainPhone")
      val captainMail = RequestHelper.fromJsonRequestToString(request, "captainMail")
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
      //Insert team & captain to DB.
      teamRepository.insert(team)
      playerRepository.insert(captain)
      println(team._id.stringify)
      Future.successful(Ok("Team created, captain created!"))
  }


  def createPlayer(teamId: String) = Action.async(parse.json) {
    request =>
      val query = new Query(Criteria where "_id" is BSONObjectID(teamId))
      val team = teamRepository.find(query).get(0)
      val name = RequestHelper.fromJsonRequestToString(request, "name")
      val surname = RequestHelper.fromJsonRequestToString(request, "surname")
      val player = DefaultPlayerImpl(name, surname)
      team.addPlayer(player)
      playerRepository.insert(player)
      teamRepository.insert(team)
      Future.successful(Ok("Player added!"))
  }


  def getTeam(id: String) = Action.async {
    val query = new Query(Criteria where "_id" is BSONObjectID(id))
    val team = teamRepository.find(query).get(0)
    Future.successful(Ok())
  }
}

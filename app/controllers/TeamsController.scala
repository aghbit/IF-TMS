package controllers

import models.enums.ListEnum
import models.exceptions.TooManyMembersInTeamException
import models.player.players.{DefaultPlayerImpl, Captain}
import models.team.teams.volleyball.volleyballs.{TeamObject, BeachVolleyballTeam}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
import repositories.{PlayerRepository, TeamRepository, TournamentRepository}
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import utils.Validators
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

      val teamName = request.body.\("teamName").validate[String](
        minLength[String](Validators.TEAM_NAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.TEAM_NAME_MAX_LENGTH)
      ).asEither

      val captainName = request.body.\("captainName").validate[String](
        minLength[String](Validators.NAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.NAME_MAX_LENGTH)
      ).asEither

      val captainSurname = request.body.\("captainSurname").validate[String](
        minLength[String](Validators.SURNAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.SURNAME_MAX_LENGTH)
      ).asEither

      val captainPhone = request.body.\("captainPhone").validate[String](
        pattern(new Regex(Validators.PHONE_REGEX))
      ).asEither

      val captainMail = request.body.\("captainMail").validate[String](
        pattern(new Regex(Validators.EMAIL_REGEX))
      ).asEither

      val inputsListEither = Map(
        "teamName" -> teamName,
        "captainName" -> captainName,
        "captainSurname" -> captainSurname,
        "captainPhone" -> captainPhone,
        "captainMail" -> captainMail
      )

      val (errors, data) = Validators.simplifyEithers(inputsListEither)

      if(errors.isEmpty){

        val captain = Captain(
          data.get("captainName").get,
          data.get("captainSurname").get,
          data.get("captainPhone").get,
          data.get("captainMail").get
        )

        //Find tournament to check discipline
        val query = new Query(Criteria where "_id" is BSONObjectID(id))
        val tournament = tournamentRepository.find(query).get(ListEnum.head)

        //Create right Team Class.
        val teamClass = "models.team.teams.volleyball.volleyballs."+
          tournament.properties.settings.discipline + "Team$"
        val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
        val module = runtimeMirror.staticModule(teamClass)
        val obj = runtimeMirror.reflectModule(module).instance.asInstanceOf[TeamObject]
        val team = obj(data.getOrElse("teamName", ""))

        //Add captain
        team.addPlayer(captain)
        team.setCaptain(captain)
        tournament.addTeam(team)

        //Insert team & captain to DB.
        try {
          teamRepository.insert(team)
          playerRepository.insert(captain)
          tournamentRepository.insert(tournament)
          Future.successful(Ok(Json.obj("id"->team._id.stringify)))
        }catch {
          case e:IllegalArgumentException => Future.successful(UnprocessableEntity("Team can't be saved!"))
          case e:Throwable => Future.failed(e)
        }
      }else {
        val jsErrors = errors.map(e => JsError.toFlatJson(e))
        Future.successful(BadRequest("Detected error: " + jsErrors))
      }
  }

  def getTeam(id: String) = Action.async {
    val query = new Query(Criteria where "_id" is BSONObjectID(id))
    val team = teamRepository.find(query).get(ListEnum.head)
    Future.successful(Ok(team.toJson))
  }

  def getTeams(id: String) = Action.async {
    request =>
      val query = new Query(Criteria where "_id" is BSONObjectID(id))
      val tournament = tournamentRepository.find(query).get(ListEnum.head)
      val teamsIDs = tournament.getTeams
      val teams = teamsIDs.map(teamID => {
        val query = new Query(Criteria where "_id" is teamID)
        teamRepository.find(query).get(ListEnum.head)
      })
      val teamsJSON = teams.map(team => team.toJson)
      Future.successful(Ok(Json.toJson(teamsJSON)))
  }

  def deleteTeam(tournId: String, teamId: String) = Action.async(parse.json) {
    request =>
      val queryTournament = new Query(Criteria where "_id" is BSONObjectID(tournId))
      val queryTeam = new Query(Criteria where "_id" is BSONObjectID(teamId))
      val tournament = tournamentRepository.find(queryTournament).get(ListEnum.head)
      val team = teamRepository.find(queryTeam).get(ListEnum.head)
      try {
        tournament.removeTeam(team)
        teamRepository.remove(team)
        tournamentRepository.insert(tournament)
        Future.successful(Ok("Team removed!"))
      }catch {
        case e: TooManyMembersInTeamException => Future.failed(e)
        case e: Throwable => Future.failed(e)
      }

  }
}

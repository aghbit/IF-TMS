package controllers

import com.mongodb.BasicDBObject
import controllers.security.AuthorizationAction
import models.enums.ListEnum
import models.exceptions.TooManyMembersInTeamException
import models.player.players.{DefaultPlayerImpl, Captain}
import models.team.Team
import models.team.teams.volleyball.volleyballs.{TeamObject, BeachVolleyballTeam}
import org.bson.types.ObjectId
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import repositories.{PlayerRepository, TeamRepository, TournamentRepository}
import utils.Validators
import scala.reflect.runtime.universe
import scala.collection.JavaConversions._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import assets.ObjectIdFormat._

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

      val captainMail = request.body.\("captainMail").validate[String](email).asEither

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
        val query = new BasicDBObject("_id", new ObjectId(id))
        val tournament = tournamentRepository.find(query).get(ListEnum.head)

        //Create right Team Class.
        val team = tournament.discipline.getNewParticipant(data.getOrElse("teamName", "")).asInstanceOf[Team]

        //Add captain
        team.addPlayer(captain)
        team.setCaptain(captain)
        tournament.addParticipant(team)

        //Insert team & captain to DB.
        try {
          teamRepository.insert(team)
          playerRepository.insert(captain)
          tournamentRepository.insert(tournament)
          Future.successful(Ok(Json.obj("id"->team._id)))
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
    val criteria = new BasicDBObject("_id", new ObjectId(id))
    val team = teamRepository.findOne(criteria).get
    Future.successful(Ok(team.toJson))
  }

  def getTeams(id: String) = AuthorizationAction.async {
    request =>
      val query = new BasicDBObject("_id", new ObjectId(id))
      val tournament = tournamentRepository.find(query).get(ListEnum.head)
      val teamsIDs = tournament.getParticipants.map(t => t._id)
      val teams = teamsIDs.map(teamID => {
        val criteria = new BasicDBObject("_id", teamID)
        teamRepository.findOne(criteria)
      })
      val teamsJSON = for(team <- teams; t<- team) yield t.toJson
      Future.successful(Ok(Json.toJson(teamsJSON)))
  }

  def deleteTeam(tournId: String, teamId: String) = Action.async(parse.json) {
    request =>
      val queryTournament = new BasicDBObject("_id", new ObjectId(tournId))
      val criteria = new BasicDBObject("_id", new ObjectId(teamId))
      val tournament = tournamentRepository.find(queryTournament).get(ListEnum.head)
      val team = teamRepository.findOne(criteria).get

      val players = team.getPlayersAsList ::: team.getBenchWarmersAsList

      try {
        players.foreach(p => playerRepository.remove(p))
        tournament.removeParticipant(team)
        teamRepository.remove(team)
        tournamentRepository.insert(tournament)
        Future.successful(Ok("Team removed!"))
      }catch {
        case e: TooManyMembersInTeamException => Future.failed(e)
        case e: Throwable => Future.failed(e)
      }
      Future.successful(Ok(""))

  }
}

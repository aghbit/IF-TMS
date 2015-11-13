package controllers

import com.mongodb.BasicDBObject
import controllers.security.TournamentAction
import models.Participant
import models.exceptions.TooManyMembersInTeamException
import models.player.Player
import models.player.players.{SinglePlayer, DefaultPlayerImpl}
import org.bson.types.ObjectId
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, JsError}
import play.api.libs.json.Reads._
import play.api.mvc.{Action, Controller}
import repositories.{PlayerRepository, TeamRepository, TournamentRepository}
import utils.Validators
import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.util.matching.Regex


/**
 * Created by Szymek Seget on 03.04.15.
 */
object PlayersController extends Controller {

  val teamRepository = new TeamRepository
  val playerRepository = new PlayerRepository
  val tournamentRepository = new TournamentRepository

  def createPlayerInTeam(teamId: String) = Action.async(parse.json) {
    request =>
      val name = request.body.\("name").validate[String](
        minLength[String](Validators.NAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.NAME_MAX_LENGTH)
      ).asEither

      val surname = request.body.\("surname").validate[String](
        minLength[String](Validators.SURNAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.SURNAME_MAX_LENGTH)
      ).asEither

      val mapEither = Map(
        "name" -> name,
        "surname" -> surname
      )

      val (errors, data) = Validators.simplifyEithers(mapEither)

      if(errors.isEmpty){

        val criteria = new BasicDBObject("_id", new ObjectId(teamId))
        val criteria2 = new BasicDBObject("participants._id", new ObjectId(teamId))
        val tournament = tournamentRepository.findOne(criteria2).get
        val team = teamRepository.findOne(criteria).get
        tournament.removeParticipant(team)
        val player = DefaultPlayerImpl(
          data.get("name").get,
          data.get("surname").get
        )
        try {
          team.addPlayer(player)
          tournament.addParticipant(team)
          playerRepository.insert(player)
          teamRepository.insert(team)
          tournamentRepository.insert(tournament)
          Future.successful(Created("Player added!"))

        }catch {
          case e:TooManyMembersInTeamException => Future.successful(UnprocessableEntity(e.getMessage))
          case e:Throwable => Future.failed(e)
        }
      }else {
        val jsErrors = errors.map(e => JsError.toFlatJson(e))
        Future.successful(BadRequest("Detected error: " + jsErrors))
      }


  }

  def createPlayerInTournament(tournamentId:String) = TournamentAction(tournamentId).async(parse.json) {
    request =>
      val name = request.body.\("playerName").validate[String](
        minLength[String](Validators.NAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.NAME_MAX_LENGTH)
      ).asEither

      val surname = request.body.\("playerSurname").validate[String](
        minLength[String](Validators.SURNAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.SURNAME_MAX_LENGTH)
      ).asEither

      val phone = request.body.\("playerPhone").validate[String](
        pattern(new Regex(Validators.PHONE_REGEX))
      ).asEither

      val mail = request.body.\("playerMail").validate[String](email).asEither

      val mapEither = Map(
        "name" -> name,
        "surname" -> surname,
        "phone" -> phone,
        "mail" -> mail
      )

      val (errors, data) = Validators.simplifyEithers(mapEither)

      if(errors.isEmpty){
        val tournament = request.tournament
        val player = tournament.discipline.getNewParticipant(data.get("name").get + " " +data.get("surname").get)
        player.asInstanceOf[SinglePlayer].name = data.get("name").get
        player.asInstanceOf[SinglePlayer].surname = data.get("surname").get
        player.asInstanceOf[SinglePlayer].phone = Some(data.get("phone").get)
        player.asInstanceOf[SinglePlayer].mail = Some(data.get("mail").get)
        try {
          tournament.addParticipant(player)
          playerRepository.insert(player.asInstanceOf[Player])
          tournamentRepository.insert(tournament)
          Future.successful(Created("Player added!"))

        }catch {
          case e:TooManyMembersInTeamException => Future.successful(UnprocessableEntity(e.getMessage))
          case e:Throwable => Future.failed(e)
        }
      }else {
        val jsErrors = errors.map(e => JsError.toFlatJson(e))
        Future.successful(BadRequest("Detected error: " + jsErrors))
      }


  }
  
  def deletePlayer(teamId: String, playerId: String) = Action.async(parse.json) {
    request =>
      val queryTeam = new BasicDBObject("_id", new ObjectId(teamId))
      val criteriaPlayer = new BasicDBObject("_id", new ObjectId(playerId))
      val team = teamRepository.findOne(queryTeam).get
      val player = playerRepository.findOne(criteriaPlayer).getOrElse(throw new Exception("Player doesn't exist!"))
      try {
        team.removePlayer(player)
        playerRepository.remove(player)
        teamRepository.insert(team)
        Future.successful(Ok("Player removed!"))
      }catch {
        case e: TooManyMembersInTeamException => Future.failed(e)
        case e: Throwable => Future.failed(e)
      }

  }

  def getPlayers(id: String) = TournamentAction(id).async {
    request =>
      val tournament = request.tournament
      val tournamentJson = tournament.getParticipants.map(p => p.toJson)
      Future.successful(Ok(Json.toJson(tournamentJson)))
  }

  def deleteSinglePlayer(id: String, playerId: String) = TournamentAction(id).async(parse.json) {
    request =>
      val tournament = request.tournament
      val player = playerRepository.findOne(new BasicDBObject("_id", new ObjectId(playerId)))
      player match {
        case Some(p) =>
          tournament.removeParticipant(p)
          tournamentRepository.insert(tournament)
          playerRepository.remove(p)
          Future.successful(Ok("Player removed!"))
        case None => Future.successful(BadRequest("Player doesn't exist."))
      }
  }
}

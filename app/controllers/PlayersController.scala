package controllers

import controllers.TeamsController._
import models.enums.ListEnum
import models.exceptions.TooManyMembersInTeamException
import models.player.players.DefaultPlayerImpl
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import play.api.libs.json.JsError
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
import utils.Validators

import scala.concurrent.Future
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._


/**
 * Created by Szymek Seget on 03.04.15.
 */
object PlayersController extends Controller {

  def createPlayer(teamId: String) = Action.async(parse.json) {
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

        val query = new Query(Criteria where "_id" is BSONObjectID(teamId))
        val team = teamRepository.find(query).get(ListEnum.head)
        val player = DefaultPlayerImpl(
          data.get("name").get,
          data.get("surname").get
        )

        try {
          team.addPlayer(player)
          playerRepository.insert(player)
          teamRepository.insert(team)
          Future.successful(Created("Player added!"))

        }catch {
          case e:TooManyMembersInTeamException => Future.failed(e)
          case e:Throwable => Future.failed(e)
        }
      }else {
        val jsErrors = errors.map(e => JsError.toFlatJson(e))
        Future.successful(BadRequest("Detected error: " + jsErrors))
      }


  }
}

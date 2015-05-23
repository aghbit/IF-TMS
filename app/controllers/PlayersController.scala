package controllers

import controllers.TeamsController._
import models.enums.ListEnum
import models.exceptions.TooManyMembersInTeamException
import models.player.players.DefaultPlayerImpl
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._


/**
 * Created by Szymek Seget on 03.04.15.
 */
object PlayersController extends Controller {

  def createPlayer(teamId: String) = Action.async(parse.json) {
    request =>
      val query = new Query(Criteria where "_id" is BSONObjectID(teamId))
      val team = teamRepository.find(query).get(ListEnum.head)
      val name = request.body.\("name").validate[String](minLength(5)).get
      val surname = request.body.\("surname").validate[String](minLength(5)).get
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

  def deletePlayer(teamId: String, playerId: String) = Action.async(parse.json) {
    request =>
      val queryTeam = new Query(Criteria where "_id" is BSONObjectID(teamId))
      val queryPlayer = new Query(Criteria where "_id" is BSONObjectID(playerId))
      val team = teamRepository.find(queryTeam).get(ListEnum.head)
      val player = playerRepository.find(queryPlayer).get(ListEnum.head)
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
}

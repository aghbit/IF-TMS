package models.team.teams.volleyball

import java.util
import assets.ObjectIdFormat
import org.bson.types.ObjectId
import play.api.libs.json.{JsArray, Json}

import scala.collection.JavaConversions._

import models.exceptions.TooManyMembersInTeamException
import models.player.Player
import models.player.players.Captain
import models.team.Team
import ObjectIdFormat._

/**
 * Created by Szymek.
 */
trait VolleyballTeams extends Team {

  val _id: ObjectId
  val playersNumber: Int
  val benchWarmersNumber: Int

  protected var players: List[Player] = List()
  protected var benchWarmers: List[Player] = List()
  protected var captain: Option[Captain] = None

  override def getMembersIDs: java.util.List[ObjectId] = {
    val result = new util.ArrayList[ObjectId]()
    val iterator = players.iterator
    while (iterator.hasNext) {
      result.add(iterator.next()._id)
    }
    val iterator2 = benchWarmers.iterator
    while (iterator2.hasNext) {
      result.add(iterator.next()._id)
    }
    result
  }

  override def getPlayersNumber:Int = playersNumber


  override def getBenchWarmersNumber: Int = benchWarmersNumber

  override def isComplete: Boolean = players.size >= playersNumber

  override def canAddPlayer: Boolean = players.size < playersNumber

  override def canAddBenchWarmer: Boolean = benchWarmers.size < benchWarmersNumber

  override def addPlayer(player: Player): Unit = {
    if (!canAddPlayer) {
      throw new TooManyMembersInTeamException("Can't add! Too many players in this team!")
    }
    players = players ::: List(player)
  }

  override def addBenchWarmer(benchWarmer: Player): Unit = {
    if (!canAddBenchWarmer) {
      throw new TooManyMembersInTeamException("Can't add! Too many bench warmers in this team!")
    }
    benchWarmers = benchWarmers ::: List(benchWarmer)
  }

  override def removePlayer(player: Player): Unit = {
    if (!players.contains(player))
      throw new NoSuchElementException("Can't remove absent player from the team!")
    players = players.filter(p => !p.equals(player))
  }

  override def removeBenchWarmer(benchWarmer: Player): Unit = {
    if (!benchWarmers.contains(benchWarmer)) {
      throw new NoSuchElementException("Can't remove absent bench warmer from the team!")
    }
    benchWarmers = benchWarmers.filter(b => !b.equals(benchWarmer))
  }

  override def setCaptain(cap: Captain): Unit = {
    if (!containsMember(cap)) {
      throw new NoSuchElementException("Captain has to be a team member!")
    }
    captain = Option(cap)
  }

  override def getCaptain: Captain = captain match {
    case Some(cap) => cap
    case None => throw new NullPointerException("Can't return captain; Captain is not set!")
  }

  override def containsMember(member: Player): Boolean = {
    players.contains(member) || benchWarmers.contains(member)
  }

  override def isReadyToSave: Boolean = captain match {
    case Some(cap) => true
    case _ => false
  }

  override def toJson = {
    val playersJsons = players.map(player => player.toJson)
    val benchWarmersJsons = benchWarmers.map(benchWarmer => benchWarmer.toJson)
    var phone:String = ""
    var mail:String = ""
    captain match {
      case Some(c:Captain) =>
        phone = c.phone
        mail = c.mail
      case _ =>
    }
    Json.obj(
      "id"->_id,
      "name"->name,
      "players"->JsArray(playersJsons),
      "benchWarmers" -> JsArray(benchWarmersJsons),
      "captain" -> (captain match {
        case Some(c:Captain) => c.toJson
        case _ => ""
      }),
      "phone" -> phone,
      "mail" -> mail
    )
  }

  override def getPlayersAsList: List[Player] = players
  override def getBenchWarmersAsList: List[Player] = benchWarmers

}

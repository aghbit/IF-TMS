package models.team.teams.volleyball

import java.util
import play.api.libs.json.{JsArray, Json}

import scala.collection.JavaConversions._

import models.exceptions.TooManyMembersInTeamException
import models.player.Player
import models.player.players.Captain
import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait VolleyballTeams extends Team {

  val _id: BSONObjectID
  val name: String
  val playersNumber: Int
  val benchWarmersNumber: Int

  protected var players: java.util.List[Player] = new util.ArrayList[Player]()
  protected var benchWarmers: java.util.List[Player] = new util.ArrayList[Player]()
  protected var captainID: Option[BSONObjectID] = None

  override def getMembersIDs: java.util.List[BSONObjectID] = {
    val result = new util.ArrayList[BSONObjectID]()
    val iterator = players.iterator()
    while (iterator.hasNext) {
      result.add(iterator.next()._id)
    }
    val iterator2 = benchWarmers.iterator()
    while (iterator2.hasNext) {
      result.add(iterator.next()._id)
    }
    result
  }

  override def isComplete: Boolean = players.size() >= playersNumber

  override def canAddPlayer: Boolean = players.size() < playersNumber

  override def canAddBenchWarmer: Boolean = benchWarmers.size() < benchWarmersNumber

  override def addPlayer(player: Player): Unit = {
    if (!canAddPlayer) {
      throw new TooManyMembersInTeamException("Can't add! Too many players in this team!")
    }
    players.add(player)
  }

  override def addBenchWarmer(benchWarmer: Player): Unit = {
    if (!canAddBenchWarmer) {
      throw new TooManyMembersInTeamException("Can't add! Too many bench warmers in this team!")
    }
    benchWarmers.add(benchWarmer)
  }

  override def removePlayer(player: Player): Unit = {
    if (!players.contains(player))
      throw new NoSuchElementException("Can't remove absent player from the team!")
    players.remove(player)
  }

  override def removeBenchWarmer(benchWarmer: Player): Unit = {
    if (!benchWarmers.contains(benchWarmer)) {
      throw new NoSuchElementException("Can't remove absent bench warmer from the team!")
    }
    benchWarmers.remove(benchWarmer)
  }

  override def setCaptain(captain: Captain): Unit = {
    if (!containsMember(captain)) {
      throw new NoSuchElementException("Captain has to be a team member!")
    }
    captainID = Option(captain._id)
  }

  override def getCaptainID: BSONObjectID = captainID match {
    case Some(captain) => captain
    case None => throw new NullPointerException("Can't return captain; Captain is not set!")
  }

  override def containsMember(member: Player): Boolean = {
    players.contains(member) || benchWarmers.contains(member)
  }

  override def isReadyToSave: Boolean = captainID match {
    case Some(captain) => true
    case _ => false
  }

  override def toJson = {
    val playersJsons = players.map(player => player.toJson)
    val benchWarmersJsons = benchWarmers.map(benchWarmer => benchWarmer.toJson)
    Json.obj(
    "id"->_id.stringify,
    "name"->name,
    "players"->JsArray(playersJsons),
    "benchWarmers" -> JsArray(benchWarmersJsons),
    "captain" -> players.filter(player=> player._id==getCaptainID).head.toJson
    )
  }
}

package models.team

import
models.player.Player
import models.player.players.Captain
import org.bson.types.ObjectId
import play.api.libs.json.JsObject

/**
 * Created by Szymek.
 */
trait Team {

  val _id: ObjectId

  val name: String

  def getMembersIDs: java.util.List[ObjectId]

  def isComplete: Boolean

  def addPlayer(player: Player)

  def addBenchWarmer(benchWarmer: Player)

  def removePlayer(player: Player)

  def removeBenchWarmer(benchWarmer: Player)

  def setCaptain(captain: Captain)

  def getCaptainID: ObjectId

  def isReadyToSave: Boolean

  def canAddPlayer: Boolean

  def canAddBenchWarmer: Boolean

  def containsMember(member: Player): Boolean

  def toJson:JsObject
}

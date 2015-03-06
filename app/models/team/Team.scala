package models.team

import models.player.Player
import models.player.players.Captain
import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait Team {

  val _id: BSONObjectID

  val name: String

  def getMembersIDs: java.util.List[BSONObjectID]

  def isComplete: Boolean

  def addPlayer(player: Player)

  def addBenchWarmer(benchWarmer: Player)

  def removePlayer(player: Player)

  def removeBenchWarmer(benchWarmer: Player)

  def setCaptain(captain: Captain)

  def getCaptainID: BSONObjectID

  def isReadyToSave: Boolean

  def canAddPlayer: Boolean

  def canAddBenchWarmer: Boolean

  def containsMember(member: Player): Boolean
}

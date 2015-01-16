package models.team

import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait Team {

  val _id: BSONObjectID

  val name: String

  def getMembersIDs: List[BSONObjectID]

  def isComplete: Boolean

  def addPlayer(player: User)

  def addBenchWarmer(benchWarmer: User)

  def removePlayer(player: User)

  def removeBenchWarmer(benchWarmer: User)

  def setCaptain(captain: User)

  def getCaptainID: BSONObjectID

  def canAddPlayer: Boolean

  def canAddBenchWarmer: Boolean

  def containsMember(member: User): Boolean
}

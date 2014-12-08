package models.team

import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait Team {

  val _id:BSONObjectID

  val name:String

  def getUsersIDs:List[BSONObjectID]

  def isComplete:Boolean

  def addPlayer(player:User)

  def removePlayer(player:User)

  def setCaptain(captain:User)

  def captainID():BSONObjectID

  def canAddPlayer:Boolean
}

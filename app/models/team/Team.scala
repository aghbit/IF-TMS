package models.team

import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait Team {

  def getUsersIDs:List[BSONObjectID]

  def isComplete:Boolean

  def addPlayer(player:User)

  def removePlayer(player:User)

  def name:String

  def setCaptain(captain:User)

  def captainID():BSONObjectID
}

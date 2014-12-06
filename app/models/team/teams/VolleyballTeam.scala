package models.team.teams

import models.team.Team
import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class VolleyballTeam(val _id:BSONObjectID,
                     val name:String,
                     val playersNumber:Int) extends Team {

  private var playersID:List[BSONObjectID] =  List()

  override def getUsersIDs: List[BSONObjectID] = playersID

  override def isComplete: Boolean = playersID.length>=playersNumber

  override def addPlayer(player: User): Unit = {
    playersID = playersID ::: List(player._id)
  }

  override def removePlayer(player: User): Unit = {
    if(!playersID.contains(player._id)){
      throw new NoSuchElementException("Can't remove absent player from the team!")
    }else{
      playersID = playersID.filter(id => id!= player._id)
    }
  }

  override def setCaptain(captain: User): Unit = {
    if(playersID.filter(user => user == captain._id).isEmpty){
      playersID = List(captain._id) ::: playersID
    }else {
      removePlayer(captain)
      playersID = List(captain._id) ::: playersID
    }
  }

  override def captainID(): BSONObjectID = {
    if(playersID.isEmpty){
      throw new NullPointerException("Can't returned captain of the empty team!")
    }else {
      playersID.head
    }
  }
}
object VolleyballTeam{
  def apply(name:String):VolleyballTeam = {
    new VolleyballTeam(BSONObjectID.generate, name, 6)
  }
}
package models.team.teams.volleyball

import models.exceptions.TooManyMembersInTeamException
import models.team.Team
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.user.User
import reactivemongo.bson.BSONObjectID

import scala.collection.immutable.List
import scala.collection.immutable.List
import scala.collection.immutable.List

/**
 * Created by Szymek.
 */
trait VolleyballTeams extends Team {

  val _id:BSONObjectID
  val name:String
  val playersNumber:Int
  val benchWarmersNumber:Int

  protected var playersID:List[BSONObjectID] =  List()
  protected var benchWarmersID:List[BSONObjectID] = List()
  protected var captainID:Option[BSONObjectID] = None

  override def getMembersIDs: List[BSONObjectID] = playersID ::: benchWarmersID

  override def isComplete: Boolean = playersID.length >= playersNumber

  override def canAddPlayer: Boolean = playersID.length<playersNumber

  override def canAddBenchWarmer: Boolean = benchWarmersID.length<benchWarmersNumber

  override def addPlayer(player: User): Unit = {
    if(!canAddPlayer){
      throw new TooManyMembersInTeamException("Can't add! Too many players in this team!")
    }
    playersID = playersID ::: List(player._id)
  }

  override def addBenchWarmer(benchWarmer:User):Unit = {
    if(!canAddBenchWarmer){
      throw new TooManyMembersInTeamException("Can't add! Too many bench warmers in this team!")
    }
    benchWarmersID = benchWarmersID ::: List(benchWarmer._id)
  }

  override def removePlayer(player: User): Unit = {
    if(!playersID.contains(player._id))
      throw new NoSuchElementException("Can't remove absent player from the team!")
    playersID = playersID.filter(id => id!= player._id)
  }

  override def removeBenchWarmer(benchWarmer:User):Unit = {
    if(!benchWarmersID.contains(benchWarmer._id)) {
      throw new NoSuchElementException("Can't remove absent bench warmer from the team!")
    }
    benchWarmersID = benchWarmersID.filter(id => id!= benchWarmer._id)
  }

  override def setCaptain(captain: User): Unit = {
    if(!containsMember(captain)){
      throw new NoSuchElementException("Captain has to be a team member!")
    }
    captainID = Option(captain._id)
  }

  override def getCaptainID: BSONObjectID = captainID match{
    case Some(captain) => captain
    case None => throw new NullPointerException("Can't return captain of the empty team!")
  }

  override def containsMember(member:User):Boolean = {
    playersID.contains(member._id) || benchWarmersID.contains(member._id)
  }

}

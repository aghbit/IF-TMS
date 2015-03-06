package models.team.teams.volleyball

import java.util

import models.exceptions.TooManyMembersInTeamException
import models.player.Player
import models.player.players.Captain
import models.team.Team
import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait VolleyballTeams extends Team {

  val _id: BSONObjectID
  val name: String
  val playersNumber: Int
  val benchWarmersNumber: Int

  protected var playersID: java.util.List[BSONObjectID] = new util.ArrayList[BSONObjectID]()
  protected var benchWarmersID: java.util.List[BSONObjectID] = new util.ArrayList[BSONObjectID]()
  protected var captainID: Option[BSONObjectID] = None

  override def getMembersIDs: java.util.List[BSONObjectID] = {
    val result = new util.ArrayList[BSONObjectID]()
    val iterator = playersID.iterator()
    while (iterator.hasNext) {
      result.add(iterator.next())
    }
    val iterator2 = benchWarmersID.iterator()
    while (iterator.hasNext) {
      result.add(iterator.next())
    }
    result
  }

  override def isComplete: Boolean = playersID.size() >= playersNumber

  override def canAddPlayer: Boolean = playersID.size() < playersNumber

  override def canAddBenchWarmer: Boolean = benchWarmersID.size() < benchWarmersNumber

  override def addPlayer(player: Player): Unit = {
    if (!canAddPlayer) {
      throw new TooManyMembersInTeamException("Can't add! Too many players in this team!")
    }
    playersID.add(player._id)
  }

  override def addBenchWarmer(benchWarmer: Player): Unit = {
    if (!canAddBenchWarmer) {
      throw new TooManyMembersInTeamException("Can't add! Too many bench warmers in this team!")
    }
    benchWarmersID.add(benchWarmer._id)
  }

  override def removePlayer(player: Player): Unit = {
    if (!playersID.contains(player._id))
      throw new NoSuchElementException("Can't remove absent player from the team!")
    playersID.remove(player._id)
  }

  override def removeBenchWarmer(benchWarmer: Player): Unit = {
    if (!benchWarmersID.contains(benchWarmer._id)) {
      throw new NoSuchElementException("Can't remove absent bench warmer from the team!")
    }
    benchWarmersID.remove(benchWarmer._id)
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
    playersID.contains(member._id) || benchWarmersID.contains(member._id)
  }

  override def isReadyToSave: Boolean = captainID match {
    case Some(captain) => true
    case _ => false
  }
}

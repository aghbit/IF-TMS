package repositories.converters

import com.mongodb.{BasicDBObject, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, Imports, MongoDBObject}
import models.strategy.{Score, Match}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import repositories.TeamRepository

/**
 * Created by Szymek Seget on 08.09.15.
 */
object MatchFromDBObjectConverter {

  private val teamsRepository = new TeamRepository

  def matchFromDBObject(dBObject: DBObject, discipline:TournamentType):Match = {
    val document:MongoDBObject = Imports.wrapDBObj(dBObject.asInstanceOf[DBObject])
    val matchID = document.getAs[Int]("_id").get

    val hostDBObject: MongoDBObject = document.getAs[MongoDBObject]("host").orNull
    val guestDBObject: MongoDBObject = document.getAs[MongoDBObject]("guest").orNull
    var host:Option[Team] = None
    if(hostDBObject != null){
      val hostDBID = hostDBObject.getAs[String]("_id").get
      val hostID = new ObjectId(hostDBID)
      val hostQuery = new BasicDBObject("_id", hostID)
      val hosts = teamsRepository.findOne(hostQuery)
      if(hosts.isDefined){
        host = Some(hosts.get)
      }
    }
    var guest:Option[Team] = None
    if(guestDBObject != null){
      val guestDBID = guestDBObject.getAs[String]("_id").get
      val guestID = new ObjectId(guestDBID)
      val guestQuery = new BasicDBObject("_id", guestID)
      val guests = teamsRepository.findOne(guestQuery)
      if(guests.isDefined){
        guest = Some(guests.get)
      }
    }

    val setsObjectList = document.getAs[MongoDBList]("sets").get

    val score:Score = discipline.getNewScore()

    val iterator = setsObjectList.iterator
    var i:Int=1

    while(iterator.hasNext){
      val set = Imports.wrapDBObj(iterator.next().asInstanceOf[DBObject]).getAs[MongoDBObject](i.toString).get
      val hostScore:Int = set.getAs[Int]("host").get
      val guestScore:Int = set.getAs[Int]("guest").get
      score.addSet()
      score.setScoreInLastSet(hostScore, guestScore)
      i=i+1
    }

    val m = new Match(matchID, host, guest, score)
    m
  }

}

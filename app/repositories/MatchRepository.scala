package repositories

import com.mongodb.DBObject
import com.mongodb.casbah.commons.{Imports, MongoDBList, MongoDBObject}
import com.mongodb.util.JSON
import configuration.CasbahMongoDBConfiguration
import models.enums.ListEnum
import models.strategy.Match
import models.strategy.scores.BeachVolleyballScore
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.{Criteria, Query}

import scala.collection.mutable

/**
 * Created by Szymek Seget on 25.05.15.
 */
class MatchRepository{
  val collectionName: String = "Matches"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  val teamsRepository = new TeamRepository()

  def insert(obj: Match): Unit = {
    collection.insert(JSON.parse(obj.toJson.toString()).asInstanceOf[DBObject])
  }
  def findOne(criteria: DBObject):Option[Match] = {
    collection.findOne(criteria) match {
      case Some(doc) => {
        val document:MongoDBObject = Imports.wrapDBObj(doc.asInstanceOf[DBObject])
        val matchID = document.getAs[Int]("_id").get

        val hostID = new ObjectId(document.getAs[MongoDBObject]("host").get.getAs[String]("_id").get)
        val guestID = new ObjectId(document.getAs[MongoDBObject]("guest").get.getAs[String]("_id").get)

        val hostQuery = new Query(Criteria where "_id" is hostID)
        val guestQuery = new Query(Criteria where "_id" is guestID)

        val host = teamsRepository.find(hostQuery).get(ListEnum.head)
        val guest = teamsRepository.find(guestQuery).get(ListEnum.head)

        val setsObjectList = document.getAs[MongoDBList]("sets").get

        val score:BeachVolleyballScore = BeachVolleyballScore()

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

        Some(new Match(matchID, Some(host), Some(guest), score))
      }
      case None => None
    }
  }

  def dropCollection() = {
    collection.dropCollection()
  }
}
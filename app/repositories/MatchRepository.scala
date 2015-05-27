package repositories

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.util.JSON
import configuration.CasbahMongoDBConfiguration
import models.strategy.Match
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek Seget on 25.05.15.
 */
class MatchRepository{
  val collectionName: String = "Matches"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  def insert(obj: Match): Unit = {
    collection.insert(JSON.parse(obj.toJson.toString()).asInstanceOf[DBObject])
  }

  def findOne(criteria: DBObject) = {
    collection.findOne(criteria) match {
      case Some(doc) => {
        val document = doc.asInstanceOf[DBObject]
        Some(document.get("host").asInstanceOf[DBObject].get("name"))
      }
      case None => None
    }
  }

  def dropCollection() = {
    collection.dropCollection()
  }
}

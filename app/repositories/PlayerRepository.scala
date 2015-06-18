package repositories

import com.mongodb.casbah.commons.{Imports, MongoDBObjectBuilder}
import configuration.CasbahMongoDBConfiguration
import models.player.Player
import models.player.players.{DefaultPlayerImpl, Captain}
import com.mongodb.{BasicDBObject, MongoException, DBObject}
import models.user.User
import org.bson.types.ObjectId
import repositories.converters.PlayerDBObjectConverter

/**
 * Created by Szymek Seget on 07.03.15.
 */
class PlayerRepository {

  val collectionName: String = "Players"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  def insert(obj:Player) = {
    val dBObject = PlayerDBObjectConverter.toDbObject(obj)
    collection.insert(dBObject)
  }

  def findOne(criteria: DBObject):Option[Player] = {
    collection.findOne(criteria) match {
      case Some(obj) => Some(PlayerDBObjectConverter.fromDbObject(obj))
      case None => None
    }
  }

  def remove(obj:Player) = {
    collection.remove(new BasicDBObject("_id", obj._id))
  }

  def dropCollection() = {
    collection.dropCollection()
  }

}

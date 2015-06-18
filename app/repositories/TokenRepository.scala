package repositories

import com.mongodb.{BasicDBObject, MongoException, DBObject}
import com.mongodb.casbah.commons.{Imports, MongoDBObjectBuilder}
import configuration.CasbahMongoDBConfiguration
import controllers.security.{TokenImpl, Token}
import models.player.Player
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 08.05.15.
 */
class TokenRepository {

  val collectionName: String = "Sessions"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  def insert(obj:Token) = {
    val builder = new MongoDBObjectBuilder
    builder += ("_id" -> ObjectId.get())
    builder += ("_class" -> obj.getClass.getName)
    builder += ("token" -> obj.getTokenAsString)
    builder += ("userId" -> obj.getUserID)
    collection.insert(builder.result())
  }

  def findOne(criteria: DBObject):Option[Token] = {
    collection.findOne(criteria) match {
      case Some(i) => {
        val document = Imports.wrapDBObj(i.asInstanceOf[DBObject])
        val userId = document.getAsOrElse[ObjectId]("userId", throw new MongoException("userId not found!"))
        val token = document.getAsOrElse[String]("token", throw new MongoException("token not found!"))
        val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not fount!"))
        className match {
          case "controllers.security.TokenImpl" => Some(TokenImpl(token, userId))
          case _ => None
        }
      }
      case None => None
    }
  }

  def remove(obj:Token) = {
    collection.remove(new BasicDBObject("userId", obj.getUserID))
  }

  def remove(criteria:DBObject) = {
    collection.remove(criteria)
  }

  def dropCollection() = {
    collection.dropCollection()
  }
}

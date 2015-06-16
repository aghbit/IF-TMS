package repositories

import com.mongodb.casbah.commons.{Imports, MongoDBObjectBuilder}
import configuration.CasbahMongoDBConfiguration
import models.player.Player
import models.player.players.{DefaultPlayerImpl, Captain}
import com.mongodb.{BasicDBObject, MongoException, DBObject}
import models.user.User
import org.bson.types.ObjectId
/**
 * Created by Szymek Seget on 07.03.15.
 */
class PlayerRepository {

  val collectionName: String = "Players"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  def insert(obj:Player) = {
    val builder = new MongoDBObjectBuilder

    builder += ("_id" -> obj._id)
    builder += ("_class" -> obj.getClass.getName)
    builder += ("name" -> obj.name)
    builder += ("surname" -> obj.surname)

    obj match {
      case objCaptain: Captain =>
        builder += ("mail" -> objCaptain.mail)
        builder += ("phone" -> objCaptain.phone)
      case _ => 
    }

    collection.insert(builder.result())
  }

  def findOne(criteria: DBObject):Option[Player] = {
    collection.findOne(criteria) match {
      case Some(obj) =>{
        val document = Imports.wrapDBObj(obj.asInstanceOf[DBObject])
        val id = document.getAsOrElse[ObjectId]("_id", throw new MongoException("_id not found!"))
        val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not found!"))
        val name = document.getAsOrElse[String]("name", throw new MongoException("name not found!"))
        val surname = document.getAsOrElse[String]("surname", throw new MongoException("surname not found!"))
        val player = className match {
          case "models.player.players.Captain" => {
            val mail = document.getAsOrElse[String]("mail", throw new MongoException("mail not found!"))
            val phone = document.getAsOrElse[String]("phone", throw new MongoException("phone not found!"))
            new Captain(id, name, surname, phone, mail)
          }
          case "models.player.players.DefaultPlayerImpl" => new DefaultPlayerImpl(id, name, surname)
          case _ => throw new Exception("Wrong class name!")
        }
        Some(player)
      }
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

package repositories.converters

import com.mongodb.{MongoException, DBObject}
import com.mongodb.casbah.commons.{Imports, MongoDBObjectBuilder}
import models.player.Player
import models.player.players.{SinglePlayer, SpeedmintonPlayer, DefaultPlayerImpl, Captain}
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 17.06.15.
 */
object PlayerDBObjectConverter {

  def toDbObject(obj:Player):DBObject = {
    val builder = new MongoDBObjectBuilder
    builder += ("_id" -> obj._id)
    builder += ("_class" -> obj.getClass.getName)
    builder += ("nickName" -> obj.getNickName)
    builder += ("name" -> obj.name)
    builder += ("surname" -> obj.surname)

    obj match {
      case objCaptain: Captain =>
        builder += ("mail" -> objCaptain.mail)
        builder += ("phone" -> objCaptain.phone)
      case objSinglePlayer: SinglePlayer =>
        for(m <- objSinglePlayer.mail) builder += ("mail" -> m)
        for(p <- objSinglePlayer.phone) builder += ("phone" -> p)
      case _ =>
    }
    builder.result()
  }

  def fromDbObject(obj:DBObject):Player = {
    val document = Imports.wrapDBObj(obj.asInstanceOf[DBObject])
    val id = document.getAsOrElse[ObjectId]("_id", throw new MongoException("_id not found!"))
    val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not found!"))
    val name = document.getAsOrElse[String]("name", throw new MongoException("name not found!"))
    val surname = document.getAsOrElse[String]("surname", throw new MongoException("surname not found!"))
    val nickName = document.getAsOrElse[String]("nickName", throw new MongoException("nickName not found!"))
    val player = className match {
      case "models.player.players.Captain" => {
        val mail = document.getAsOrElse[String]("mail", throw new MongoException("mail not found!"))
        val phone = document.getAsOrElse[String]("phone", throw new MongoException("phone not found!"))
        new Captain(id, name, surname, phone, mail)
      }
      case "models.player.players.DefaultPlayerImpl" => new DefaultPlayerImpl(id, name, surname)
      case "models.player.players.SpeedmintonPlayer" => {
        val p = SpeedmintonPlayer(id, nickName)
        p.name = name
        p.surname = surname
        p.phone = document.getAs[String]("phone")
        p.mail = document.getAs[String]("mail")
        p
      }
      case _ => throw new Exception("Wrong class name!")
    }
    player
  }
}

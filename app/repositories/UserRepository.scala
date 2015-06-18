package repositories

import com.mongodb.casbah.commons.{Imports, MongoDBObjectBuilder}
import com.mongodb.util.JSON
import com.mongodb.{BasicDBObject, DBObject, MongoException}
import configuration.CasbahMongoDBConfiguration
import models.exceptions.UserWithThisLoginExistsInDB
import models.user.User
import models.user.userproperties.JsonFormat._
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.bson.types.ObjectId
import play.api.libs.json.Json

/**
 * Created by Szymek.
 */
class UserRepository {

  val collectionName: String = "Users"


  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)
  @throws[UserWithThisLoginExistsInDB]
  def insert(obj: User): Unit = {

    if(findOne(new BasicDBObject("personalData.login", obj.getProperties.login)).isEmpty){
      val builder = new MongoDBObjectBuilder()
      builder += ("_id" -> obj._id)
      builder += ("_class" -> obj.getClass.getName)
      builder += ("personalData" -> JSON.parse(Json.toJson(obj.getProperties).toString()))
      builder += ("isAdmin" -> obj.isAdmin)
      builder += ("isBanned" -> obj.isBanned)
      collection.insert(builder.result())
    }else {
      throw new UserWithThisLoginExistsInDB("User with this login exists in DB!")
    }

  }

  def findOne(criteria: DBObject):Option[User] = {
    collection.findOne(criteria) match {
      case Some(dbObj) => {

        val document = Imports.wrapDBObj(dbObj.asInstanceOf[DBObject])
        val id = document.getAs[ObjectId]("_id").getOrElse(throw new MongoException("Can't find _id!"))
        val personalDataJson = Json.parse(document.getAs[BasicDBObject]("personalData").getOrElse(
          throw new MongoException("Can't find personalData!")
        ).toString)
        val personalData = personalDataJson.validate[UserProperties].getOrElse(
          throw new MongoException("User properties parsing error!")
        )

        val obj = document.getAs[String]("_class").getOrElse(throw new MongoException("Can't find _class!"))
        obj match {
          case "models.user.users.userimpl.UserImpl" => Some(new UserImpl(id, personalData))
          case _ => throw new Exception("Wrong class name!")
        }
      }
      case None => None
    }
  }

  def remove(user: User) = {
    collection.remove(new BasicDBObject("_id", user._id))
  }

  def dropCollection(): Unit = {
    collection.dropCollection()
  }
}

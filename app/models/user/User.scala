package models.user

import controllers.security.{TokenImpl, Token}
import models.user.userproperties.UserProperties
import org.bson.types.ObjectId
import play.api.libs.json.{JsObject, Json}
import models.user.userproperties.JsonFormat._
import assets.ObjectIdFormat._

/**
 * Created by Szymek.
 */
trait User {

  def isBanned: Boolean

  def isActive: Boolean

  def isAdmin: Boolean

  def _id: ObjectId

  def activateAccount: Boolean

  def getProperties: UserProperties

  def toJson = {
    Json.obj(
    "id"-> _id,
    "userProperties"-> (Json.toJson(getProperties).as[JsObject] - "password")
    )
  }

  def generateToken :Token = {
     TokenImpl(_id)
  }

}

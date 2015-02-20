package models.user

import controllers.security.{TokenImpl, Token}
import models.user.userproperties.UserProperties
import play.api.libs.json.{JsObject, Json}
import reactivemongo.bson.BSONObjectID
import models.user.userproperties.JsonFormat._

/**
 * Created by Szymek.
 */
trait User {

  def _id: BSONObjectID

  def activateAccount: Boolean

  def getProperties: UserProperties

  def toJson: String = {
    val stringBuilder = new StringBuilder
    stringBuilder.append("{\"id\": ")
    stringBuilder.append(_id.stringify)
    stringBuilder.append(", ")
    val userPropertiesJson = Json.toJson(getProperties)
    stringBuilder.append(userPropertiesJson.as[JsObject] - "password")
    stringBuilder.append("}")
    stringBuilder.result()
  }

  def generateToken :Token = {
     new TokenImpl(_id)
  }

}

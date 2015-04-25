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

  def toJson = {
    Json.obj(
    "id"-> _id.stringify,
    "userProperties"-> (Json.toJson(getProperties).as[JsObject] - "password")
    )
  }

  def generateToken :Token = {
     new TokenImpl(_id)
  }

}

package models.player

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by szymek on 06.03.15.
 */
trait Player {

  val _id:BSONObjectID
  val name:String
  val surname:String

  def toJson = {
    Json.obj(
    "id" -> _id.stringify,
    "name" -> name,
    "surname" -> surname
    )
  }

}

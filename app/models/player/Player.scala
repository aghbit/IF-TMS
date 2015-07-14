package models.player

import play.api.libs.json.Json
import org.bson.types.ObjectId
import assets.ObjectIdFormat._
/**
 * Created by szymek on 06.03.15.
 */
trait Player {

  val _id:ObjectId
  val name:String
  val surname:String

  def toJson = {
    Json.obj(
    "id" -> _id,
    "name" -> name,
    "surname" -> surname
    )
  }

  override def equals(obj: scala.Any): Boolean = obj match {
    case null => false
    case p:Player => _id.equals(p._id)
    case _ => false
  }
}

package models.player

import models.Participant
import play.api.libs.json.Json
import org.bson.types.ObjectId
import assets.ObjectIdFormat._
/**
 * Created by szymek on 06.03.15.
 */
trait Player extends Participant{

  val _id:ObjectId
  var name:String
  var surname:String

  override def getNickName: String

  override def isReadyToSave: Boolean = name!=null && surname!=null

  def toJson = {
    Json.obj(
    "id" -> _id,
    "nickName" -> getNickName,
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

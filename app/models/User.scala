package models

import play.modules.reactivemongo.json.BSONFormats._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID


/**
 * Created by Szymek.
 */
case class User(_id:Option[BSONObjectID], name:String, age: Int, active:Boolean) {
  override def toString = "[Name: " + name + ", Age: " + age.toString + " ]"

}
object JsonFormat {
  implicit val userFormat = Json.format[User]
}

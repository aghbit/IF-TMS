package models

import org.bson.types.ObjectId
import play.api.libs.json.JsObject

/**
 * Created by Szymek Seget on 17.09.15.
 */
trait Participant {

  val _id: ObjectId

  def getNickName:String
  def toJson:JsObject
  def isReadyToSave: Boolean

}

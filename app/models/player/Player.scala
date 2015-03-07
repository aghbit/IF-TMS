package models.player

import reactivemongo.bson.BSONObjectID

/**
 * Created by szymek on 06.03.15.
 */
trait Player {

  val _id:BSONObjectID
  val name:String
  val surname:String

  def toJson = {
    val builder = new StringBuilder()
    builder.append("{id: ")
    builder.append(_id.stringify)
    builder.append(", name: ")
    builder.append(name)
    builder.append(", surname: ")
    builder.append(surname)
    builder.append("}")
    builder.toString()
  }

}

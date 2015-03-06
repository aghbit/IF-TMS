package models.player

import reactivemongo.bson.BSONObjectID

/**
 * Created by szymek on 06.03.15.
 */
trait Player {

  val _id:BSONObjectID
  val name:String
  val surname:String

}

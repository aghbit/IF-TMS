package models.player.players

import models.player.Player
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek Seget on 06.03.15.
 */
case class Captain(_id:BSONObjectID,
                   name:String,
                   surname:String,
                   phone:String,
                   mail:String) extends Player {

  /**
   * Only for Spring Data. Don't use it. For more information check: TMS-76
   */
  def this() = this(null, null, null, null, null)
}
object Captain {
  def apply(name:String, surname:String, phone:String, mail:String) = {
    new Captain(BSONObjectID.generate, name, surname, phone, mail)
  }
}

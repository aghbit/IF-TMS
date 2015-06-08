package models.player.players

import models.player.Player
import org.bson.types.ObjectId
/**
 * Created by Szymek Seget on 06.03.15.
 */
case class Captain(_id:ObjectId,
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
    new Captain(ObjectId.get(), name, surname, phone, mail)
  }
}

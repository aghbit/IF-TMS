package models.player.players

import models.player.Player
import org.bson.types.ObjectId
/**
 * Created by Szymek Seget on 06.03.15.
 */
case class Captain(_id:ObjectId,
                   override var name:String,
                   override var surname:String,
                   phone:String,
                   mail:String) extends Player {

  override def getNickName: String = name+" "+surname

  override def isReadyToSave: Boolean = name != null && surname != null
}
object Captain {
  def apply(name:String, surname:String, phone:String, mail:String) = {
    new Captain(ObjectId.get(), name, surname, phone, mail)
  }

}

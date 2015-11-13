package models.player.players

import models.player.Player
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 06.03.15.
 */
case class DefaultPlayerImpl(_id:ObjectId,
                             override var name:String,
                             override var surname:String) extends Player {

  override def getNickName: String = name + " " + surname

  override def isReadyToSave: Boolean = name != null && surname != null
}

object DefaultPlayerImpl {
  def apply(name:String, surname:String) = new DefaultPlayerImpl(ObjectId.get(), name, surname)
}

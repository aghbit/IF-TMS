package models.player.players

import models.player.Player
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 06.03.15.
 */
case class DefaultPlayerImpl(_id:ObjectId,
                             name:String,
                             surname:String) extends Player {

}

object DefaultPlayerImpl {
  def apply(name:String, surname:String) = new DefaultPlayerImpl(ObjectId.get(), name, surname)
}

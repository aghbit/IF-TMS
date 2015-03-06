package models.player.players

import models.player.Player
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek Seget on 06.03.15.
 */
case class DefaultPlayerImpl(_id:BSONObjectID,
                             name:String,
                         surname:String) extends Player {

}

object DefaultPlayerImpl {
  def apply(name:String, surname:String) = new DefaultPlayerImpl(BSONObjectID.generate, name, surname)
}

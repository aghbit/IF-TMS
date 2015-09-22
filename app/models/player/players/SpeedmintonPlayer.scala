package models.player.players

import models.player.Player
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 22.09.15.
 */
class SpeedmintonPlayer(override val _id: ObjectId, val nickName:String) extends Player{
  var name: String = _
  var surname: String = _

  override def getNickName: String = nickName

  override def isReadyToSave: Boolean = nickName != null
}
object SpeedmintonPlayer {
  def apply(nickName:String) = new SpeedmintonPlayer(new ObjectId(), nickName)
  def apply(id:ObjectId, nickName:String) = new SpeedmintonPlayer(id, nickName)
}
package models.player.players

import models.player.Player
import play.api.libs.json.{Json, JsObject}

/**
 * Created by Szymek Seget on 26.09.15.
 */
trait SinglePlayer extends Player{

  var phone: Option[String] = None
  var mail: Option[String] = None

  override def toJson: JsObject = {
    var json = super.toJson
    phone match {
      case Some(p) => json = json + ("phone", Json.toJson(p))
      case _ =>
    }
    mail match {
      case Some(p) => json = json + ("mail", Json.toJson(mail))
      case _ =>
    }
    json
  }
}

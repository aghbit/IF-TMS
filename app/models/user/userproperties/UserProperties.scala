package models.user.userproperties

import play.api.libs.json._


/**
 * Created by Szymek.
 */
case class UserProperties(name: String,
                          login: String,
                          password: String,
                          phone: String,
                          mail: String) {

}
object JsonFormat {
  implicit val userPropertiesFormat = Json.format[UserProperties]
}
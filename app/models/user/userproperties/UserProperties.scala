package models.user.userproperties

import play.api.libs.json._
import play.api.libs.functional.syntax._


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
  implicit val userPropertiesFormat:Format[UserProperties] = (
    (JsPath \ "name").format[String] and
      (JsPath \ "login").format[String] and
      (JsPath \ "password").format[String] and
      (JsPath \ "phone").format[String] and
      (JsPath \ "mail").format[String](email keepAnd )
    )(UserProperties.apply, unlift(UserProperties.unapply))
}
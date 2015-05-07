package models.user.userproperties

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

import scala.util.matching.Regex


/**
 * Created by Szymek.
 */
case class UserProperties(name: String,
                          login: String,
                          password: String,
                          phone: String,
                          mail: String) {


    /*
  Only for Spring Data. Don't use it. For more information check: TMS-76
   */
  def this() = this(null, null, null, null, null)


}
object JsonFormat {
  implicit val userPropertiesFormat:Format[UserProperties] = (
    (JsPath \ "name").format[String](minLength[String](3) andKeep maxLength[String](20)) and
      (JsPath \ "login").format[String](minLength[String](5) andKeep maxLength[String](20)) and
      (JsPath \ "password").format[String](minLength[String](5) andKeep maxLength[String](20)) and
      (JsPath \ "phone").format[String](minLength[String](9) andKeep maxLength[String](9)
        andKeep pattern(new Regex("^[0-9]+$"), "error.regex")) and
      (JsPath \ "mail").format[String](email)
    )(UserProperties.apply, unlift(UserProperties.unapply))
}
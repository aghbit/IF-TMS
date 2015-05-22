package models.user.userproperties

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import utils.Validators

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
    (JsPath \ "name").format[String](minLength[String](Validators.NAME_MIN_LENGTH) andKeep
                                                        maxLength[String](Validators.NAME_MAX_LENGTH)) and
      (JsPath \ "login").format[String](minLength[String](Validators.LOGIN_MIN_LENGTH) andKeep
                                                          maxLength[String](Validators.LOGIN_MAX_LENGTH)) and
      (JsPath \ "password").format[String](minLength[String](Validators.PASSWORD_MIN_LENGTH) andKeep
                                                              maxLength[String](Validators.PASSWORD_MAX_LENGTH)) and
      (JsPath \ "phone").format[String](pattern(new Regex(Validators.PHONE_REGEX), "error.regex")) and
      (JsPath \ "mail").format[String](email)
    )(UserProperties.apply, unlift(UserProperties.unapply))
}
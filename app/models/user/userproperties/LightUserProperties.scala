package models.user.userproperties

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import utils.Validators

import scala.util.matching.Regex



/**
 * Created by Ludwik.
 */
case class LightUserProperties(name: String,
                          login: String,
                          phone: String,
                          mail: String) {


  /*
Only for Spring Data. Don't use it. For more information check: TMS-76
 */
  def this() = this(null, null, null, null)


}
object JsonFormat {
  implicit val userPropertiesFormat:Format[LightUserProperties] = (
    (JsPath \ "name").format[String](minLength[String](Validators.NAME_MIN_LENGTH) andKeep
      maxLength[String](Validators.NAME_MAX_LENGTH)) and
      (JsPath \ "login").format[String](minLength[String](Validators.LOGIN_MIN_LENGTH) andKeep
        maxLength[String](Validators.LOGIN_MAX_LENGTH)) and
      (JsPath \ "phone").format[String](pattern(new Regex(Validators.PHONE_REGEX), "error.regex")) and
      (JsPath \ "mail").format[String](email)
    )(LightUserProperties.apply, unlift(LightUserProperties.unapply))
}
package controllers.helpers

import play.api.libs.json.JsValue
import play.api.mvc.Request

/**
 * Created by Szymek Seget on 07.03.15.
 */
object RequestHelper {

  def fromJsonRequestToString(request:Request[JsValue] ,fieldName:String) = {
    request.body.\(fieldName).toString().replace("\"","")
  }

}

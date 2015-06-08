package assets

/**
*  Created by Szymek Seget on 31.05.15.
*/
import org.bson.types.ObjectId
import play.api.libs.json._

object  ObjectIdFormat {

  implicit  val objectIdFormat:Format[ObjectId] = new Format[ObjectId] {

    override def writes(o: ObjectId): JsValue = {
      JsString(o.toString)
    }

    override def reads(json: JsValue): JsResult[ObjectId] = {
      json match {
        case jsString: JsString =>
          if(ObjectId.isValid(jsString.value)) {
            JsSuccess(new ObjectId(jsString.value))
          }
            else {
            JsError("Invalid ObjectId")
          }
        case other => JsError("Can't parse json path as an ObjectId. Json content = " +other.toString)
      }
    }
  }
}

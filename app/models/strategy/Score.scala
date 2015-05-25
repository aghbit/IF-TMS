package models.strategy


import play.api.libs.json.JsObject
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-07.
 */
trait Score {

  /**
   * Return JsObject representation. e.g.
   * {"score": {
   *          sets: [{"1": {
   *                        "host":21,
   *                        "guest":15}},
   *                 {"2": {
   *                        "host":21,
   *                        "guest":10}},
   *                 {"3": {
   *                        "host":null,
   *                        "guest":null}}
   *                ]
   *           }
   * }
   * @return
   */
  def toJson:JsObject
}

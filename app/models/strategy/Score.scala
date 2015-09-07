package models.strategy


import play.api.libs.json.JsObject

trait Score {

  def isHostWinner:Boolean

  def isGuestWinner:Boolean

  def isMatchFinished:Boolean

  def addSet():Unit

  def setScoreInLastSet(hostScore:Int, guestScore:Int):Unit

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

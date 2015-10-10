package models.strategy


import models.strategy.scores.newscores.PointsContainer
import play.api.libs.json.{Json, JsObject}

class Score(val maxPointsContainersNumber:Int) {

 var pointsContainers:List[PointsContainer] = List()

  private val defaultNumberOfContainersToWin:Int = maxPointsContainersNumber/2 + 1

  def isHostWinner:Boolean = {
    var hostContainersNumber = 0
    pointsContainers.foreach(
    p => if(p.isHostWinner) {
      hostContainersNumber = hostContainersNumber + 1
    })
    hostContainersNumber == defaultNumberOfContainersToWin
  }

  def isGuestWinner:Boolean = {
    var hostContainersNumber = 0
    pointsContainers.foreach(
      p => if(p.isGuestWinner) {
        hostContainersNumber = hostContainersNumber + 1
      })
    hostContainersNumber == defaultNumberOfContainersToWin
  }

  def isMatchFinished:Boolean = {
    isHostWinner || isGuestWinner
  }

  def addPointsContainer(pointsContainer: PointsContainer):Unit = {
    require(pointsContainers.length < maxPointsContainersNumber, "Can't add points container.")
    pointsContainers = pointsContainers ::: List(pointsContainer)
  }

  /**
   * Return JsObject representation. e.g.
   * {"score": {
   *          pointsContainers: [{"1": {
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
  def toJson:JsObject = {
    Json.obj("score" ->
      Json.obj("pointsContainers" ->
        Json.toJson(pointsContainers.zipWithIndex.map{ case (p, i) => Json.obj(i.toString -> p.toJson)})
      )
    )
  }
}

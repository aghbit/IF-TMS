package models.strategy.scores.newscores

import play.api.libs.json.JsObject

/**
 * Created by Szymek Seget on 27.09.15.
 */
class Gems(val withTieBreak:Boolean) extends PointsContainer {

  private var hostGems:List[Gem] = List()
  private var guestGems:List[Gem] = List()
  private var tieBreak:Option[TieBreak] = _

  override def isHostWinner: Boolean = {
    (hostGems.length == 6 && guestGems.length <=4) ||
      (hostGems.length == 7 && guestGems.length == 5) ||
      (tieBreak match {
        case Some(t) => t.isHostWinner
        case None => false
      })
  }

  override def isDraw: Boolean = false

  override def isGuestWinner: Boolean = {
    (guestGems.length == 6 && hostGems.length <=4) ||
      (guestGems.length == 7 && hostGems.length == 5) ||
      (tieBreak match {
        case Some(t) => t.isGuestWinner
        case None => false
      })
  }

  override def toJson: JsObject = ???

  def addHostGem(gem: Gem) = {
    hostGems = hostGems ::: List(gem)
  }

  def addGuestGem(gem: Gem) = {
    guestGems = guestGems ::: List(gem)
  }

  def addTieBreak(tieBreak: TieBreak) = {
    this.tieBreak = Some(tieBreak)
  }
}

package models.strategy.scores.newscores

import models.strategy.scores.newscores.GemScore.GemScore

/**
 * Created by Szymek Seget on 27.09.15.
 */
class Gem(var hostScore:GemScore, var guestScore:GemScore) {
  def isFinished = hostScore.equals(GemScore.GAME) || guestScore.equals(GemScore.GAME)
  def isHostWinner = hostScore.equals(GemScore.GAME)
  def isGuestWinner = guestScore.equals(GemScore.GAME)
}
object Gem {
  def apply() = new Gem(GemScore.LOVE, GemScore.LOVE)
  def apply(hostScore:GemScore, guestScore:GemScore) = new Gem(hostScore, guestScore)
}

object GemScore extends Enumeration {
  type GemScore = Value
  val LOVE = Value("0")
  val FIFTEEN = Value("15")
  val THIRTY = Value("30")
  val FORTY = Value("40")
  val GAME = Value("Game")
}

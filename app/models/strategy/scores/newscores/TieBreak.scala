package models.strategy.scores.newscores

/**
 * Created by Szymek Seget on 27.09.15.
 */
class TieBreak {

  def isHostWinner:Boolean = ???

  def isGuestWinner:Boolean = ???
}
object TieBreakScore extends Enumeration {
  type TieBreakScore = Value
  val ZERO = Value("0")
  val ONE = Value("1")
  val TWO = Value("2")
  val THREE = Value("3")
  val FOUR = Value("4")
  val FIVE = Value("5")
  val SIX = Value("6")
  val SEVEN = Value("7")
  val EIGHT = Value("8")
  val NINE = Value("9")
  val TEN = Value("10")
}
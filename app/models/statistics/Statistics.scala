package models.statistics

/**
 * Created by Szymek.
 * Edited by krzysiek
 */
trait Statistics {
  val discipline:String
  val pointUnit:String // ex. point/goal

  def getDiscipline:String = {
    discipline
  }
  def getPointUnit:String = {
    pointUnit
  }
}

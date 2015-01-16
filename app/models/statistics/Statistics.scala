package models.statistics

/**
 * Created by Szymek.
 * Edited by krzysiek
 */
trait Statistics {
  val discipline: String
  // BUG discipline should be enum
  val pointUnit: String // ex. point/goal
}

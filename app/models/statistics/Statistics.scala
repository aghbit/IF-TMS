package models.statistics

import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 * Edited by krzysiek
 */
trait Statistics {
  val _id: BSONObjectID
  val discipline: String
  val pointUnit: String // ex. point/goal
}

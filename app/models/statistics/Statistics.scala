package models.statistics

import org.bson.types.ObjectId


/**
 * Created by Szymek.
 * Edited by krzysiek
 */
trait Statistics {
  val _id: ObjectId
  val discipline: String
  val pointUnit: String // ex. point/goal
}

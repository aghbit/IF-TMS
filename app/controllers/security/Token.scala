package controllers.security

import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait Token {

  def getUserID:BSONObjectID

  override def equals(obj: scala.Any): Boolean

  override def hashCode(): Int

  override def toString: String

}

package controllers.security

import org.bson.types.ObjectId
/**
 * Created by Szymek.
 */
trait Token {

  def getUserID:ObjectId

  override def equals(obj: scala.Any): Boolean

  override def hashCode(): Int

  def getTokenAsString: String

}

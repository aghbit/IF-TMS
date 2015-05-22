package controllers.security

import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
case class TokenImpl(token: String, userID:BSONObjectID) extends Token {

  def this() = {
    this(null, null)
  }

  override def getUserID: BSONObjectID = userID

  override def equals(obj: Any): Boolean = obj match {
    case t: TokenImpl => t.token == this.token
    case _ => false
  }

  override def hashCode(): Int = {
    token.hashCode
  }

  override def toString: String = {
    token
  }
}
object TokenImpl {
  def apply(userID:BSONObjectID) = {
    new TokenImpl(userID.stringify + System.nanoTime(), userID)
  }
  def apply(token:String) = {
    new TokenImpl(token, BSONObjectID(token.substring(0, 24)))
  }
}


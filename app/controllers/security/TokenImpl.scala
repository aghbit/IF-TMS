package controllers.security

import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class TokenImpl(val token: String) extends Token {

  def this(userID: BSONObjectID) = {
    this(userID.stringify + System.nanoTime())
  }

  override def getUserID: BSONObjectID = BSONObjectID(token.substring(0, 24))

  override def equals(obj: scala.Any): Boolean = obj match {
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


package controllers.security
import org.bson.types.ObjectId
/**
 * Created by Szymek.
 */
case class TokenImpl(token: String, userID:ObjectId) extends Token {

  def this() = {
    this(null, null)
  }

  override def getUserID: ObjectId = userID

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
  def apply(userID:ObjectId) = {
    new TokenImpl(userID.toString + System.nanoTime(), userID)
  }
  def apply(token:String) = {
    val tokenObj = token.substring(0, 24)
    if(ObjectId.isValid(tokenObj)){
      new TokenImpl(token, new ObjectId(tokenObj))
    }else{
      throw new IllegalArgumentException("This token is not valid.")
    }
  }
}


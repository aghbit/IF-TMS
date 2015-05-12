package controllers.security

import org.springframework.data.mongodb.core.query.Query
import reactivemongo.bson.BSONObjectID
import repositories.TokenRepository
import scala.collection.JavaConversions._

/**
 * Created by Szymek.
 */
object TokensKeeper {

  private var tokens = Map[BSONObjectID, Token]()
  private val repository = new TokenRepository()

  def addToken(token:Token) = {
    val exists = tokens.get(token.getUserID)
    exists match {
      case Some(s) => removeTokenForUser(s.getUserID)
      case None =>
    }
    tokens = tokens + ((token.getUserID, token))
    repository.insert(token)
  }

  def removeTokenForUser(id: BSONObjectID): Unit ={
    val token = tokens.get(id)
    repository.remove(token)
    tokens = tokens.filterKeys(u => u!= id)
  }

  def containsToken(token:Token): Boolean ={
    if(tokens.contains(token.getUserID)){
      true
    }
    else {
      tokens = repository.find(new Query()).map(t => (t.getUserID, t)).toMap
      tokens.contains(token.getUserID)
    }
  }

  def removeAllTokens() = {
    tokens = Map[BSONObjectID, Token]()
    repository.dropCollection()
  }

  def getTokensNumber = {
    tokens.size
  }

}

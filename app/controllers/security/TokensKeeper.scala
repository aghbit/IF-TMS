package controllers.security

import org.springframework.data.mongodb.core.query.{Criteria, Query}
import reactivemongo.bson.BSONObjectID
import repositories.TokenRepository
import scala.collection.JavaConversions._

/**
 * Created by Szymek.
 */
object TokensKeeper {

  private var tokens = List[Token]()
  private val repository = new TokenRepository()

  def addToken(token:Token) = {
    if(tokens.exists(t => t.getUserID.equals(token.getUserID))){
      removeTokenForUser(token.getUserID)
    }
    tokens = tokens ::: List[Token](token)
    repository.insert(token)
  }

  def removeTokenForUser(id: BSONObjectID): Unit ={
    val query = new Query(Criteria where "userID" is id)
    repository.remove(query)
    tokens = tokens.filter(t => !t.getUserID.equals(id))
  }

  def containsToken(token:Token): Boolean ={
    if(tokens.exists(t => t.getUserID.equals(token.getUserID))){
      true
    }else {
      tokens = repository.find(new Query()).toList
      tokens.exists(t => t.getUserID.equals(token.getUserID))
    }
  }

  def removeAllTokens() = {
    tokens = List()
    repository.dropCollection()
  }

  def getTokensNumber = {
    tokens.size
  }
}

package controllers.security

import com.mongodb.BasicDBObject
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.{Criteria, Query}
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

  def removeTokenForUser(id: ObjectId): Unit ={
    val criteria = new BasicDBObject("userId", id)
    repository.remove(criteria)
    tokens = tokens.filter(t => !t.getUserID.equals(id))
  }

  def containsToken(token:Token): Boolean = {
    if(tokens.exists(t => t.getUserID.equals(token.getUserID))){
      true
    }else {
      tokens = repository.findOne(new BasicDBObject()).toList
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

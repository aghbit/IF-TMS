package controllers.security

/**
 * Created by Szymek.
 */
object TokensKeeper {

  private var tokens = List[Token]()

  def addToken(token:Token) = {
    if(tokens.map(t => t.getUserID).contains(token.getUserID)){
      val n = tokens.map(t => t.getUserID).indexOf(token.getUserID)
      removeToken(tokens(n))
    }
    tokens = tokens ::: List(token)
  }

  def removeToken(token:Token) = {
    tokens = tokens.filter(t => t!=token)
  }

  def containsToken(token:Token): Boolean ={
    tokens.contains(token)
  }

  def removeAllTokens() = {
    tokens = List[Token]()
  }

  def getTokensNumber() = {
    tokens.size
  }

}

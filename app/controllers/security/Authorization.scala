package controllers.security

import play.api.mvc._

import scala.concurrent.Future


/**
 * Created by Szymek.
 */
case class Authorization[A](action: Action[A]) extends Action[A] with Controller{

  def apply(request: Request[A]):Future[Result] = {
    request.headers.get("token") match {
      case Some(t) => {
        val token = new TokenImpl(t)
        if(tokensKeeper.containsToken(token)){
          action(request)
        }else {
          Future.successful(Ok("Zabroniony dostęp!"))
        }
      }
      case _ => Future.successful(Ok("Zabroniony dostęp!"))
    }
  }

  override def parser: BodyParser[A] = action.parser
}
object AuthorizationAction extends ActionBuilder[Request]{
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    block(request)
  }
  override protected def composeAction[A](action: Action[A]): Action[A] = new Authorization[A](action)
}
object tokensKeeper {

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

}
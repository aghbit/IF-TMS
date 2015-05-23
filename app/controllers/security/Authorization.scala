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
        val token = TokenImpl(t)
        if(TokensKeeper.containsToken(token)){
          action(request)
        }else {
          Future.successful(Unauthorized("Forbidden, you have to be logged in!"))
        }
      }
      case _ => Future.successful(Unauthorized("Forbidden, you have to be logged in!"))
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

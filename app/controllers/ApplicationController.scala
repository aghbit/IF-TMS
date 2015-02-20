package controllers

import controllers.security.{TokenImpl, tokensKeeper}
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.MongoController
import reactivemongo.bson.BSONObjectID


object ApplicationController extends Controller with MongoController {

  def index = Action {
    Ok(views.html.index())
  }
  def authenticate(login:String, password: String) = Action{ request =>
    if(login == "login" && password =="haslo"){
      val token = new TokenImpl(BSONObjectID.generate)
      println(token.toString)
      tokensKeeper.addToken(token)
      Ok("token123")
    }
    else{
      Unauthorized("Fail to sign in")
    }
  }
  def statistics(token: String) = Action{request=>
    if(token == "token123"){
      Ok("zawartosc strony ")
    }
    else{
      Unauthorized("Unauthorized")
    }
  }
}
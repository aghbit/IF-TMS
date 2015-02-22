package controllers

import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.MongoController


object ApplicationController extends Controller with MongoController {

  def index = Action {
    Ok(views.html.index())
  }
  def authenticate(login:String, password: String) = Action{ request =>
    if(login == "login" && password =="haslo"){
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
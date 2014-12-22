package controllers

import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.MongoController


object ApplicationController extends Controller with MongoController{

  def index = Action {

    Ok(views.html.index())
  }

}
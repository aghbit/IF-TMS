package controllers

import controllers.security.{TokenImpl, tokensKeeper}
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.MongoController
import reactivemongo.bson.BSONObjectID


object ApplicationController extends Controller with MongoController {

  def index = Action {
    Ok(views.html.index())
  }

}
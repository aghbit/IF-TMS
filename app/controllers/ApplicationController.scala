package controllers

import controllers.security.{TokenImpl, TokensKeeper}
import play.api.mvc.{Action, Controller}


object ApplicationController extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

}
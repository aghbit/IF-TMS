package controllers

import controllers.security.{TokenImpl, TokensKeeper}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future


object ApplicationController extends Controller {

  def index = Action.async {
    Future.successful(Ok(views.html.index()))
  }

}
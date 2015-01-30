package controllers

import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.MongoController
import repositories.{TestModel, TestModelRepository}


object ApplicationController extends Controller with MongoController {

  def index = Action {
    val repo = new TestModelRepository()
    val o = new TestModel
    o.m
    repo.insert(o)
    Ok(views.html.index())
  }

}
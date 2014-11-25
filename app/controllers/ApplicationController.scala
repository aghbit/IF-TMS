package controllers

import models.User
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global
import models.JsonFormat._

import scala.concurrent.Future
import play.modules.reactivemongo.json.BSONFormats._


object ApplicationController extends Controller with MongoController{

  def collection: JSONCollection = db.collection("u")

  def index = Action {
    Ok(views.html.index())
  }

  def get(id:String) = Action.async {
    val query = id match{
      case "" => Json.obj("active" -> true)
      case s: String => Json.obj("active" -> true, "_id" -> BSONObjectID(s))
    }

    val cursor = collection.find(query).cursor[User].collect[List]()
    cursor.map {
      case ul :: List()  => if(id != "") Ok(Json.toJson(ul)) else Ok(Json.toJson(List(ul)))
      case ul => Ok(Json.toJson(ul))
    }
  }

  def delete() = Action.async(parse.json) {
    request =>
      val id = request.body.validate[User]
      val value = id.map(user => user._id.get)
      val value2 = value.get
      println(value2)
      collection.remove(Json.obj("_id" -> value2))
      Future.successful(Ok("Removed!"))
  }

  def post() = Action.async(parse.json){
    request =>
      val user = request.body.validate[User]
      val userMem = user.get
      collection.insert(Json.toJson(userMem))
      Future.successful(Ok(userMem.toString + " Async"))

  }

  def put() = Action.async(parse.json){
    request =>
      val user = request.body.validate[User]
      val userMem = user.get
      collection.save(Json.toJson(userMem))
      Future.successful(Ok(userMem.toString + " Async"))
  }
}
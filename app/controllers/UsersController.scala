package controllers

import controllers.security.{AuthorizationAction, TokenImpl, TokensKeeper}
import models.enums.ListEnum
import models.exceptions.UserWithThisLoginExistsInDB
import models.user.userproperties.JsonFormat._
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
import repositories.UserRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global



object UsersController extends Controller{

  private val repository = new UserRepository()

  def createUser() = Action.async(parse.json){
    request =>
      val userProperties = request.body.validate[UserProperties]
      val user = UserImpl(userProperties.get)
      try {
        repository.insert(user)
        Future.successful(Created)
      } catch {
        case e:UserWithThisLoginExistsInDB => Future.failed(e)
      }
  }

  def getUser(id: String) = AuthorizationAction.async {
    request => {
      val token = TokenImpl(request.headers.get("token").get)
      val userID = token.getUserID
      if(userID.stringify == id){
        val query = new Query(Criteria where "_id" is BSONObjectID(id))
        val users = repository.find(query)
        Future.successful(Ok(users.get(ListEnum.head).toJson))
      }else {
        Future.successful(Unauthorized("You are not authorized to see this content!"))
      }
    }
  }

  def login() = Action.async(parse.json) {
    request =>

      val login = request.body.\("login").validate[String].get
      val password = request.body.\("password").validate[String].get
      val query = new Query(Criteria where "personalData.login" is login and "personalData.password" is password)
      val users = repository.find(query)
      if(users.isEmpty){
        Future.successful(Unauthorized("Wrong login or password!"))
      }else{
        val token = users.get(ListEnum.head).generateToken
        TokensKeeper.addToken(token)
        Future.successful(Ok(token.toString))
      }
  }

  def isLoginInUse(login: String) = Action.async{
    request =>
      val query = new Query(Criteria where "personalData.login" is login)
      val users = repository.find(query)
      if(users.isEmpty){
        Future(NotFound("Login is not in use."))
      }else {
        Future(Ok("Login is in use."))
      }
  }
}
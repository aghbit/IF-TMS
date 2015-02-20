package controllers

import java.math.BigInteger

import controllers.security.{TokenImpl, tokensKeeper, AuthorizationAction}
import models.exceptions.UserWithThisLoginExistsInDB
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
import repositories.UserRepository
import models.user.userproperties.JsonFormat._
import org.springframework.data.mongodb.core.query.{Criteria, Query}

import scala.concurrent.Future


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

  def getUser(id: String) = Action.async {
    val query = new Query(Criteria where "_id" is BSONObjectID(id))
    val users = repository.find(query)
    Future.successful(Ok(users.get(0).toJson))
  }

  def test() = AuthorizationAction.async {
    request =>
      val userID = new TokenImpl(request.headers.get("token").get).getUserID
      val query = new Query(Criteria where "_id" is userID)
      println(userID.stringify)
      val user = repository.find(query).get(0)
      Future.successful(Ok("Cześć "+user.personalData.name+"!").withHeaders("token"->"<uuuAleTokenuuu>"))
  }

  def login() = Action.async(parse.json) {
    request =>
      val login = request.body.\("login").toString().replaceAll("\"", "")
      val password = request.body.\("password").toString().replaceAll("\"", "")
      val query = new Query(Criteria where "personalData.login" is login and "personalData.password" is password)
      val users = repository.find(query)
      if(!users.isEmpty){
        val token = users.get(0).generateToken
        tokensKeeper.addToken(token)
        Future.successful(Ok(token.toString))
      }else{
        Future.successful(Unauthorized("Wrong login or password!"))
      }
  }
}
package controllers

import controllers.security.{AuthorizationAction, TokenImpl, TokensKeeper}
import models.enums.ListEnum
import models.exceptions.UserWithThisLoginExistsInDB
import models.user.userproperties.JsonFormat._
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import play.api.libs.json.JsError
import play.api.libs.json.Reads._
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONObjectID
import repositories.UserRepository
import play.api.libs.functional.syntax._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global



object UsersController extends Controller{

  private val repository = new UserRepository()

  def createUser() = Action.async(parse.json){
    request =>
      val userProperties = request.body.validate[UserProperties].asEither
      userProperties match {
        case Right(uP) =>
          val user = UserImpl(uP)
          try {
            repository.insert(user)
            Future.successful(Created)
          } catch {
            case e:UserWithThisLoginExistsInDB => Future.failed(e)
            case e:Exception => Future.failed(e)
          }
        case Left(e) => Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(e)))
      }
  }

  def getUser(id: String) = AuthorizationAction.async {
    request => {
      request.headers.get("token") match {
        case Some(s) =>
          val token = TokenImpl(s)
          val userID = token.getUserID
          userID.stringify match {
            case `id` =>
              val query = new Query(Criteria where "_id" is BSONObjectID(id))
              val users = repository.find(query)
              Future.successful(Ok(users.get(ListEnum.head).toJson))
            case _ =>
              Future.successful(Unauthorized("You are not authorized to see this content!"))
          }
          //This should never execute, because AuthorizationAction checked the token.
        case None => Future.successful(NotFound("User with this ID was not found!"))
      }
    }
  }

  def login() = Action.async(parse.json) {
    request =>
      val login = request.body.\("login").validate[String](minLength[String](5)
                                                          andKeep maxLength[String](20)).asEither
      val password = request.body.\("password").validate[String](minLength[String](5)
                                                                andKeep maxLength[String](20)).asEither
      (login, password) match {
        case(Right(l), Right(p)) =>
          val query = new Query(Criteria where "personalData.login" is l and
            "personalData.password" is p)
          val users = repository.find(query)
          if(users.isEmpty){
            Future.successful(Unauthorized("Wrong login or password!"))
          }else{
            val token = users.get(ListEnum.head).generateToken
            TokensKeeper.addToken(token)
            Future.successful(Ok(token.toString))
          }
        case (Right(l), Left(p)) =>
          Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(p)))
        case (Left(l), Right(p)) =>
          Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(l)))
        case (Left(l), Left(p)) =>
          Future.successful(BadRequest("Detected error: " + JsError.toFlatJson(l) + " "
                                        + JsError.toFlatJson(p)))
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
package controllers

import com.mongodb.{BasicDBObjectBuilder, BasicDBObject}
import controllers.security.{AuthorizationAction, TokenImpl, TokensKeeper}
import models.enums.ListEnum
import models.exceptions.UserWithThisLoginExistsInDB
import models.user.userproperties.JsonFormat._
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import play.api.libs.json.JsError
import play.api.libs.json.Reads._
import play.api.mvc.{Action, Controller}
import repositories.UserRepository
import play.api.libs.functional.syntax._
import utils.Validators

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
          userID match {
            case u:ObjectId =>
              val criteria = new BasicDBObject("_id", u)
              val users = repository.findOne(criteria)
              Future.successful(Ok(users.get.toJson))
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
      val login = request.body.\("login").validate[String](
        minLength[String](Validators.LOGIN_MIN_LENGTH) andKeep
        maxLength[String](Validators.LOGIN_MAX_LENGTH)
      ).asEither

      val password = request.body.\("password").validate[String](
        minLength[String](Validators.PASSWORD_MIN_LENGTH) andKeep
          maxLength[String](Validators.PASSWORD_MAX_LENGTH)
      ).asEither

      val inputsListEither = Map("login" -> login, "password" -> password)

      val (errors, data) = Validators.simplifyEithers(inputsListEither)

      if(errors.isEmpty){
        val builder = new BasicDBObjectBuilder()
        builder.append("personalData.login", data.get("login").get)
        builder.append("personalData.password", data.get("password").get)
        val users = repository.findOne(builder.get())
        if(users.isEmpty){
          Future.successful(Unauthorized("Wrong login or password!"))
        }else{
          val token = users.head.generateToken
          TokensKeeper.addToken(token)
          Future.successful(Ok(token.toString))
        }
      }else {
        val jsErrors = errors.map(e=> JsError.toFlatJson(e))
        Future.successful(BadRequest("Detected error: " + jsErrors))
      }
  }

  def isLoginInUse(login: String) = Action.async{
    request =>
      val criteria = new BasicDBObject("personalData.login", login)
      val users = repository.findOne(criteria)
      if(users.isEmpty){
        Future(NotFound("Login is not in use."))
      }else {
        Future(Ok("Login is in use."))
      }
  }
}
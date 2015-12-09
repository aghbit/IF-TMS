package controllers

import com.mongodb.{BasicDBObjectBuilder, BasicDBObject}
import controllers.security.{AuthorizationAction, TokenImpl, TokensKeeper}
import models.exceptions.UserWithThisLoginExistsInDB
import models.user.userproperties.JsonFormat._
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.bson.types.ObjectId
import play.api.libs.json.JsError
import play.api.libs.json.Reads._
import play.api.mvc.{Action, Controller}
import repositories.UserRepository
import play.api.libs.functional.syntax._
import utils.Validators
import scala.util.matching.Regex


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

  def modifyUser(id: String) = Action.async(parse.json) {
    request =>
      val name = request.body.\("name").validate[String](
        minLength[String](Validators.NAME_MIN_LENGTH) andKeep
          maxLength[String](Validators.NAME_MAX_LENGTH)
      ).asEither

      val login = request.body.\("login").validate[String](
        minLength[String](Validators.LOGIN_MIN_LENGTH) andKeep
          maxLength[String](Validators.LOGIN_MAX_LENGTH)
      ).asEither

      val phone = request.body.\("phone").validate[String](
        pattern(new Regex(Validators.PHONE_REGEX))
      ).asEither

      val mail = request.body.\("mail").validate[String](
        pattern(new Regex(Validators.EMAIL_REGEX))
      ).asEither

      val rawData = Map(
        "name" -> name,
        "login" -> login,
        "phone" -> phone,
        "mail" -> mail
      )

      val (errors, data) = Validators.simplifyEithers(rawData)

      if(errors.isEmpty) {

        val criteria = new BasicDBObject("_id", id)
        val user = repository.findOne(criteria).get
        val password = user.getProperties.password

        val newUserProperties = UserProperties(
          data.get("name").get,
          data.get("login").get,
          password,
          data.get("phone").get,
          data.get("mail").get
        )

        val newUser = UserImpl(newUserProperties)

        try {
          repository.remove(user)
          repository.insert(newUser)
          val token = newUser.generateToken
          TokensKeeper.addToken(token)
          Future.successful(Ok(token.toString))
        }
        catch {
          case e: Exception => Future.failed(e)
        }

      }
      else {
        val jsErrors = errors.map(e => JsError.toFlatJson(e))
        Future.successful(BadRequest("Detected error: " + jsErrors))
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
          Future.successful(Ok(token.getTokenAsString))
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
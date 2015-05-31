package models.userimpl

import models.user.userproperties.JsonFormat._
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import play.api.libs.json.{JsObject, Json}

/**
 * Created by Piotr on 2014-12-13.
 */
@RunWith(classOf[JUnitRunner])
class UserImplTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var instance: UserImpl = _
  var instance2: UserImpl = _
  var userProperties: UserProperties = _


  before {
    userProperties = mock[UserProperties]
    instance = new UserImpl(ObjectId.get, userProperties)
    instance2 = new UserImpl(ObjectId.get, mock[UserProperties])
    instance2.isAdmin = true
  }


  test("Default account status is NotActive") {
    //given
    //when
    val test1: Boolean = instance.isActive
    val test2: Boolean = instance.isBanned

    //then
    assert(!test1, "Status is NotActive test")
    assert(!test2, "Status is Banned test")
  }

  test("Activation account works") {

    //given
    instance.activateAccount

    //when
    val test1: Boolean = instance.isActive
    val test2: Boolean = instance.isBanned

    //then
    assert(test1, "Status is  Active test")
    assert(!test2, "Status is  not Banned test")
  }


  test("Setting admin when not admin") {
    //given
    //when & then
    intercept[IllegalStateException] {
      instance.setAdmin(instance2)
    }

  }

  test("Setting admin") {
    //given
    //when
    instance2.setAdmin(instance)
    val test = instance.isAdmin

    //then
    assert(test, "Admin setting completed")
  }

  test("Banning admin") {

    //given
    //when & then
    intercept[IllegalStateException] {
      instance.banUser(instance2)
    }


  }

  test("Banning user") {

    //given
    instance.isActive = true
    //when
    instance2.banUser(instance)
    val test = instance.isBanned

    //then
    assert(test, "Ban completed")

  }

  test("Get Properties") {

    //given

    //when

    //then
    assert(instance.personalData == userProperties, "User properties does't work!")

  }

  test("Simple toJson test") {

    //given
    Mockito.when(userProperties.login).thenReturn("Login")
    Mockito.when(userProperties.mail).thenReturn("Mail")
    Mockito.when(userProperties.name).thenReturn("Name")
    Mockito.when(userProperties.password).thenReturn("Password")
    Mockito.when(userProperties.phone).thenReturn("Phone")
    val userPropertiesJson = Json.toJson(userProperties)
    val result = "{\"id\":\"" + instance._id.toString + "\",\"userProperties\":"+ userPropertiesJson + "}"
    val userPropertiesJsonWithoutPassword = userPropertiesJson.as[JsObject] - "password"
    val resultWithoutPassword = "{\"id\":\"" + instance._id.toString + "\",\"userProperties\":"+ userPropertiesJsonWithoutPassword + "}"

    //when
    val instanceResult = instance.toJson

    //then
    assert(result !== instanceResult.toString(), "toJson: test 1")
    assert(resultWithoutPassword === instanceResult.toString(), "toJson: test2")
  }

  test("Simple generateToken test") {

    //given

    //when
    val token = instance.generateToken

    //then
    assert(token.getUserID === instance._id, "generateToken: test 1")

  }
}

package models.userimpl

import models.statistics.Statistics
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Piotr on 2014-12-13.
 */
@RunWith(classOf[JUnitRunner])
class UserImplTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var instance: UserImpl = _
  var instance2: UserImpl = _


  before {
    instance = new UserImpl(BSONObjectID.generate, mock[UserProperties], mock[Option[Statistics]])
    instance2 = new UserImpl(BSONObjectID.generate, mock[UserProperties], mock[Option[Statistics]])
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

}

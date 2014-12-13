package user.users.userimpl

import models.statistics.Statistics
import models.user.userproperties.UserProperties
import models.user.users.UserType
import models.user.users.userimpl.UserImpl
import models.user.userstatus.UserStatus
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
    instance2.userType = UserType.Admin
  }


  test("Default account status is NotActive") {
    //given
    //when
    val test1: Boolean = instance.isAccountNotActive
    val test2: Boolean = instance.isAccountActive
    val test3: Boolean = instance.isAccountBanned

    //then
    assert(test1, "Status is NotActive test")
    assert(!test2, "Status is not Active test")
    assert(!test3, "Status is not Banned test")
  }

  test("Activation account works") {

    //given
    instance.activateAccount

    //when
    val test1: Boolean = instance.isAccountNotActive
    val test2: Boolean = instance.isAccountActive
    val test3: Boolean = instance.isAccountBanned

    //then
    assert(!test1, "Status is not NotActive test")
    assert(test2, "Status is  Active test")
    assert(!test3, "Status is not Banned test")
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
    val test = instance.userType

    //then
    assert(test === UserType.Admin, "Admin setting completed")
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
    instance.status = UserStatus.Active

    //when
    instance2.banUser(instance)
    val test = instance.status

    //then
    assert(test === UserStatus.Banned, "Ban completed")

  }

}

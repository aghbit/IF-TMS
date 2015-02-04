package IT.repositories

import models.user.User
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.springframework.data.mongodb.core.query._
import repositories.UserRepository

/**
 * Created by Szymek.
 */
class UserRepositoryTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var underTest:UserRepository = _
  var user:User = _

  before{
    underTest = new UserRepository()
    underTest.initTest()
    user = UserImpl(new UserProperties("Name", "Login", "Password", "Phone", "Mail"), new UserS)
  }

  test("Simple test"){

    //given
    val query = new Query(Criteria where "_id" is user._id)

    //when
    underTest.insert(user)
    //val userRestored = underTest.find(query).get(0)

    //then
    //assert(userRestored._id == user._id, "Simple test: test 1")
  }
}

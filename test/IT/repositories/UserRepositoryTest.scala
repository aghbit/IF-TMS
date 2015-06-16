package IT.repositories

import com.mongodb.BasicDBObject
import models.exceptions.UserWithThisLoginExistsInDB
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

  var underTest: UserRepository = _
  var user: User = _
  var userProperties: UserProperties = _

  before {
    underTest = new UserRepository()
    userProperties = new UserProperties("Szymon", "Passarinho", "Password", "784588999", "test@test.pl")
    user = UserImpl(userProperties)
  }

  after {
    underTest.dropCollection()
  }

  test("Simple test") {

    //given
    val criteria = new BasicDBObject("_id", user._id)

    //when
    underTest.insert(user)
    val userRestored = underTest.findOne(criteria).get

    //then
    assert(userRestored.getClass == classOf[UserImpl], "Simple test: Wrong class type!")
    assert(userRestored._id == user._id, "Simple test: Wrong id!")
    assert(userRestored.getProperties.name == "Szymon", "Simple test: Wrong name!!")
    assert(userRestored.getProperties.login == "Passarinho", "Simple test: Wrong login!!")
    assert(userRestored.getProperties.password == "Password", "Simple test: Wrong Password!!")
    assert(userRestored.getProperties.phone == "784588999", "Simple test: Wrong Phone!!")
    assert(userRestored.getProperties.mail == "test@test.pl", "Simple test: Wrong Mail!!")

    //when
    underTest.remove(user)

    val userRestored2 = underTest.findOne(criteria)

    //then
    assert(userRestored2.isEmpty, "Simple test: Remove error!")

  }

  test("Add two users with the same login. Should throw an exception."){

    //given
    val user2 = UserImpl(userProperties)
    underTest.insert(user)

    //when&then
    intercept[UserWithThisLoginExistsInDB]{
      underTest.insert(user2)
    }

  }
}

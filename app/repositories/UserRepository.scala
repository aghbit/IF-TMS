package repositories

import models.exceptions.{NoUserWithThisLoginInDB, UserWithThisLoginExistsInDB}
import models.user.User
import models.user.users.userimpl.UserImpl
import org.springframework.data.mongodb.core.query.{Criteria, Query}

/**
 * Created by Szymek. Edited by Ludwik.
 */
class UserRepository extends Repository[User] {

  override val collectionName: String = "Users"
  override val clazz: Class[User] = classOf[User]

  @throws[UserWithThisLoginExistsInDB]
  override def insert[T](obj: T) = {
    val user = obj.asInstanceOf[User]
    val query = new Query(Criteria where "personalData.login" is user.getProperties.login)
    val usersList = super.find(query)
    if(!usersList.isEmpty) {
      throw new UserWithThisLoginExistsInDB("User with login: " + user.getProperties.login + " exists in DB!")
    } else {
      mongoTemplate.insert(obj, collectionName)
    }
  }
  @throws[NoUserWithThisLoginInDB]
  override def remove[T](obj: T) = {
    val user = obj.asInstanceOf[User]
    val query = new Query(Criteria where "personalData.login" is user.getProperties.login)
    val usersList = super.find(query)
    if(usersList.isEmpty) {
      throw new NoUserWithThisLoginInDB("User with login: " + user.getProperties.login + " not exists in DB!")
    } else {
      mongoTemplate.remove(obj, collectionName)
    }
  }

}

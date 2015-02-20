package repositories

import models.exceptions.UserWithThisLoginExistsInDB
import models.user.User
import models.user.users.userimpl.UserImpl
import org.springframework.data.mongodb.core.query.{Criteria, Query}

/**
 * Created by Szymek.
 */
class UserRepository extends Repository[UserImpl] {

  override val collectionName: String = "Users"
  override val clazz: Class[UserImpl] = classOf[UserImpl]

  override def insert[T](obj: T): Unit = {
    val user = obj.asInstanceOf[UserImpl]
    val query = new Query(Criteria where "personalData.login" is user.getProperties.login)
    val usersList = super.find(query)
    if(!usersList.isEmpty) {
      throw new UserWithThisLoginExistsInDB("User with login: " + user.getProperties.login + " exists in DB!")
    }
    mongoTemplate.insert(obj, collectionName)
  }

}

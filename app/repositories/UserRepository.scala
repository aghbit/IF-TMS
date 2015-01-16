package repositories

import models.user.User
import models.user.users.userimpl.UserImpl
import org.springframework.data.mongodb.core.query.Query

/**
 * Created by Szymek.
 */
class UserRepository extends Repository{

  def find(query: Query, clazz: Class[UserImpl], collection: String) = {
    mongoTemplate.find(query, clazz, collection)
  }

  def insert(user: User) = {
    mongoTemplate.save(user, "Users")
  }

  def remove(query: Query, clazz: Class[UserImpl], collection: String) = {
    mongoTemplate.findAllAndRemove(query, clazz, collection)
  }
}

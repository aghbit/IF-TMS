package repositories

import models.user.User
import org.springframework.data.mongodb.core.query.Query

/**
 * Created by Szymek.
 */
class UserRepository extends Repository {

  val collectionName: String = "Users"
  val clazz = classOf[User]

  def find(query: Query) = {
    mongoTemplate.find(query, clazz, collectionName)
  }

  def insert(user: User) = {
    mongoTemplate.save(user, "Users")
  }

  def remove(user: User) = {
    mongoTemplate.remove(user, collectionName)
  }
}

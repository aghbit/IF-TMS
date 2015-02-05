package repositories

import models.user.User
import org.springframework.data.mongodb.core.query.Query

/**
 * Created by Szymek.
 */
class UserRepository extends Repository[User] {

  override val collectionName: String = "Users"
  override val clazz: Class[User] = classOf[User]
}

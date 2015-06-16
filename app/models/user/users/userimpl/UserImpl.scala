package models.user.users.userimpl

import models.user.User
import models.user.userproperties.UserProperties
import models.user.users.AbstractUser
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by Piotr on 2014-12-12.
 */

case class UserImpl(override val _id: ObjectId,
               override val personalData: UserProperties)
  extends AbstractUser(_id, personalData, isAdmin = false, isActive = false, isBanned = false) {

}


object UserImpl {
  def apply(personalData: UserProperties): User = {
    new UserImpl(ObjectId.get(), personalData)
  }
}

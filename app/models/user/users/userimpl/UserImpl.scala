package models.user.users.userimpl

import models.user.User
import models.user.userproperties.UserProperties
import models.user.users.AbstractUser
import reactivemongo.bson.BSONObjectID

/**
 * Created by Piotr on 2014-12-12.
 */
class UserImpl(override val _id: BSONObjectID,
               override val personalData: UserProperties)
  extends AbstractUser(_id, personalData, isAdmin = false, isActive = false, isBanned = false) {
}


object UserImpl {
  def apply(personalData: UserProperties): User = {
    new UserImpl(BSONObjectID.generate, personalData)
  }
}

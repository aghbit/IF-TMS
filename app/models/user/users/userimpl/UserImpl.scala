package models.user.users.userimpl

import models.user.User
import models.user.userproperties.UserProperties
import models.user.users.AbstractUser
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by Piotr on 2014-12-12.
 */

class UserImpl(override val _id: ObjectId,
               override val personalData: UserProperties)
  extends AbstractUser(_id, personalData, isAdmin = false, isActive = false, isBanned = false) {

  /*
  Only for Spring Data. Don't use it. For more information check: TMS-76
   */
  def this() = this(null, null)
}


object UserImpl {
  def apply(personalData: UserProperties): User = {
    new UserImpl(ObjectId.get(), personalData)
  }
}

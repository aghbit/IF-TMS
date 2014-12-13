package models.user.users.userimpl

import models.statistics.Statistics
import models.user.userproperties.UserProperties
import models.user.users.AbstractUser
import reactivemongo.bson.BSONObjectID

/**
 * Created by Piotr on 2014-12-12.
 */
class UserImpl(override val _id: BSONObjectID,
               override val personalData: UserProperties,
               override val statistics: Option[Statistics]) extends AbstractUser(_id, personalData, statistics) {

}


object UserImpl {
  private def apply(personalData: UserProperties, statistics: Option[Statistics]): Unit = {
    new UserImpl(BSONObjectID.generate, personalData, statistics)
  }
}

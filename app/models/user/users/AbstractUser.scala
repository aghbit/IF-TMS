package models.user.users

import models.statistics.Statistics
import models.user.User
import models.user.userproperties.UserProperties
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
abstract class AbstractUser(val _id: BSONObjectID,
                            val personalData: UserProperties,
                            val statistics: Option[Statistics],
                            var isAdmin: Boolean,
                            var isActive: Boolean,
                            var isBanned: Boolean) extends User {


  def setAdmin(user: AbstractUser): Unit = {
    if (user.isAdmin) {
      throw new IllegalStateException("User is already admin")
    }
    user.isAdmin = true
  }


  def activateAccount: Boolean = {
    if (!isActive) {
      isActive = true
      return true
    }
    false
  }

  def banUser(user: AbstractUser): Unit = {
    if (user.isAdmin) {
      throw new IllegalStateException("Cannot ban admin")
    }
    if (!user.isActive) {
      throw new IllegalStateException("Cannot ban nonActive user")
    }
    if (user.isBanned) {
      throw new IllegalStateException("Cannot ban banned user")
    }
    if (!isAdmin) {
      throw new IllegalStateException("NonAdmin User Cannot ban users")
    }
    user.isBanned = true

  }


}

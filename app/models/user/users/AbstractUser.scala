package models.user.users

import models.statistics.Statistics
import models.team.Team
import models.tournament.tournaments.Tournament
import models.user.User
import models.user.userproperties.UserProperties
import models.user.userstatus.UserStatus
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
abstract class AbstractUser(val _id: BSONObjectID,
                            val personalData: UserProperties,
                            val statistics: Option[Statistics],
                            var userType: UserType.Value = UserType.User,
                            var status: UserStatus.Value = UserStatus.NotActive) extends User {

  def isAccountActive: Boolean = status == UserStatus.Active

  def isAccountNotActive: Boolean = status == UserStatus.NotActive

  def isAccountBanned: Boolean = status == UserStatus.Banned

  def isAdmin: Boolean = userType == UserType.Admin

  def setAdmin(user: AbstractUser): Unit = {
    if(user.isAdmin) throw new IllegalStateException("User is already admin")
    user.userType = UserType.Admin

  }


  def activateAccount: Boolean = {
    if (isAccountNotActive) {
      status = UserStatus.Active
      return true
    }
    false
  }

  def banUser(user: AbstractUser): Unit = {
    if(user.userType == UserType.Admin) throw new IllegalStateException("Cannot ban admin")
    if(user.status != UserStatus.Active ) throw new IllegalStateException("Cannot ban nonActive or banned user")

    user.status = UserStatus.Banned
  }


}

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

  def setAdmin(user: AbstractUser): Boolean = {
    if(!this.isAdmin || user.isAdmin) return false
    user.userType = UserType.Admin
    true
  }


  def activateAccount: Boolean = {
    if (isAccountNotActive) {
      status = UserStatus.Active
      return true
    }
    false
  }

  def banUser(user: AbstractUser): Boolean = {
    if(this.userType == UserType.Admin && user.status == UserStatus.Active && user.userType != UserStatus.Active) {
      user.status = UserStatus.Banned
      return true
    }
    false
  }


}

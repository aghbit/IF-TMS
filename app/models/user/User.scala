package models.user

import models.user.userstatus.UserStatus
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait User {

    def _id:BSONObjectID

    def isAccountActive: Boolean

    def isAccountNotActive: Boolean

    def isAccountBanned: Boolean

    def isAdmin: Boolean

    def activateAccount: Boolean

}

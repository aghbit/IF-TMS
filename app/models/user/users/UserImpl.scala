package models.user.users

import models.statistics.Statistics
import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class UserImpl(override val _id:BSONObjectID,
                val personalData: UserProperties,
                val statistics: Statistics) extends User
{

}

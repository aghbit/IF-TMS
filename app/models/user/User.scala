package models.user

import models.user.userproperties.UserProperties
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait User {

  def _id: BSONObjectID

  def activateAccount: Boolean

  def getProperties: UserProperties

}

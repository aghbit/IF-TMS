package models.user

import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
trait User {

  def _id: BSONObjectID

  def activateAccount: Boolean

}

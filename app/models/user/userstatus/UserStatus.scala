package models.user.userstatus

/**
 * Created by Piotr on 2014-12-12.
 */


object UserStatus extends Enumeration {
  type State = Value
  val NotActive, Active, Banned = Value

}

case class UserStatus(enum: UserStatus)
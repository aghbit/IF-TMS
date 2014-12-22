package models.user.users

/**
 * Created by Piotr on 2014-12-13.
 */
object UserType extends Enumeration{
  type State = Value
  val User, Admin = Value

}

case class UserType(enum: UserType)
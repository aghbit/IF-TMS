package utils


/**
 * Created by Szymek Seget on 20.05.15.
 */
object Validators {
  val LOGIN_MIN_LENGTH = 5
  val NAME_MIN_LENGTH = 3
  val SURNAME_MIN_LENGTH  = 3
  val PASSWORD_MIN_LENGTH = 5
  val PHONE_REGEX = "^[0-9]{9}$"
  val LOGIN_MAX_LENGTH = 20
  val NAME_MAX_LENGTH = 20
  val SURNAME_MAX_LENGTH  = 30
  val PASSWORD_MAX_LENGTH = 20
  val TEAM_NAME_MIN_LENGTH = 5
  val TEAM_NAME_MAX_LENGTH = 20

  def simplifyEithers[L, R](map:Map[String, Either[L, R]]) = {
    val rights = map.collect{case (s:String, Right(e)) => (s, e)}
    val lefts = map.collect{case (s:String, Left(e)) => e}
    (lefts, rights)
  }
}

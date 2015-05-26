package models.exceptions

/**
 * Created by ludwik on 24.05.15.
 */
class NoUserWithThisLoginInDB(s:String) extends Exception(s) {
  def this() = this(null);

}

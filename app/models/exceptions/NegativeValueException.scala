package models.exceptions

/**
 * Created by krzysiek.
 */
class NegativeValueException(s: String) extends Exception(s) {

  /**
   * Only for Spring Data. Don't use it. For more information check: TMS-76
   */
  def this() = this(null)

}

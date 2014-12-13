package models.strategy

/**
 * Created by Rafal on 2014-12-12.
 */
class VSet() {
  var points = 0
  var won:Boolean = false
}
object VSet{
  def apply() = new VSet()
}
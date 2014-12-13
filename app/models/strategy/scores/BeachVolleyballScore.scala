package models.strategy.scores

/**
 * Created by Rafal on 2014-12-08.
 */
class BeachVolleyballScore(override val maxSets:Int) extends VolleyballScore(maxSets) {

  override val maxPoints = 21

}

object BeachVolleyballScore{
  def apply(maxSets:Int = 2) = new BeachVolleyballScore(maxSets)
}

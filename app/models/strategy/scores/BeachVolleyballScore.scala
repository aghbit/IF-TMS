package models.strategy.scores

import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-08.
 */
class BeachVolleyballScore(override val host:Option[BSONObjectID],
                           override val guest:Option[BSONObjectID],
                           override val maxSets:Int) extends VolleyballScore(host,guest,maxSets) {

  override val maxPoints = 21

}

object BeachVolleyballScore{
  def apply(host:Option[BSONObjectID],guest:Option[BSONObjectID],maxSets:Int = 2) = new BeachVolleyballScore(host,guest,maxSets)
}

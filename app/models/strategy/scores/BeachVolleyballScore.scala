package models.strategy.scores

import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-08.
 */
class BeachVolleyballScore(override val host: BSONObjectID,
                           override val guest: BSONObjectID,
                           override val maxSets: Int) extends VolleyballScore(host, guest, maxSets) {

  override val maxPoints = 21

}

object BeachVolleyballScore {
  def apply(host: BSONObjectID, guest: BSONObjectID, maxSets: Int = 3) = new BeachVolleyballScore(host, guest, maxSets)
}

package models.strategy

import models.strategy.scores.BeachVolleyballScore
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import play.api.libs.json.{Json, JsObject}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek Seget on 2014-12-02.
 */
class Match (val id:BSONObjectID,
             var host:Option[Team],
             var guest:Option[Team],
             var score:Score) {

  /**
   * Returns json version of this match.
   * Json e.g.
   * {"_id": BSONObjectID.stringify,
   *  "host": {
   *            "id":BSONObjectID.stringify,
   *            "name":"teamName"},
   *  "guest": {
   *             "id":BSONObjectID.stringify,
   *             "name":"teamName"},
   *  "score": {
   *            sets: [{"1": {
   *                        "host":21,
   *                        "guest":15}},
   *                   {"2": {
   *                        "host":21,
   *                        "guest":10}},
   *                   {"3": {
   *                        "host":null,
   *                        "guest":null}}
   *                   ]
   *             }
   * }
   *
   * @return jsObject
   */
  def toJson:JsObject = {
    Json.obj("_id" -> id.stringify,
    "host" -> Json.obj("_id" -> host.orNull._id.stringify, "name" -> host.orNull.name),
    "guest" -> Json.obj("_id" -> guest.orNull._id.stringify, "name" -> guest.orNull.name))
    .++(score.toJson)
  }



}
object Match {
  def apply(host:Option[Team], guest:Option[Team], tournamentType: TournamentType) = {
    new Match(BSONObjectID.generate, host, guest, tournamentType.getNewScore())
  }
}
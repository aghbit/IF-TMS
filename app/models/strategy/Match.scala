package models.strategy

import models.team.Team
import models.tournament.tournamenttype.TournamentType
import play.api.libs.json.{JsObject, Json}

/**
 * Created by Szymek Seget on 2014-12-02.
 */
class Match (var id:Int,
             var host:Option[Team],
             var guest:Option[Team],
             var score:Score) {


  /**
   * Adds team to this match. First check canAddTeam(), if this return false throw IllegalStateException.
   * Otherwise adds team (passed as argument) to this team. First add as host, if host exists add as guest.
   * @param team
   */
  def addTeam(team: Option[Team]): Unit = {
    if(!canAddTeam()){
      throw new IllegalStateException("Can't add team to this Match!!")
    }
    host match {
      case Some(i) => {
        guest match {
          case Some(g) =>
          case None => guest = team
        }
      }
      case None => host = team
    }
  }

  def getWinner(): Option[Team] = {
    if(score.isHostWinner())
      host
    else
      guest
  }

  def getLoser(): Option[Team] = {
    if(score.isHostWinner()){
      guest
    }else{
      host
    }
  }

  def canAddTeam():Boolean = {
    (host, guest) match {
      case (Some(h), Some(g)) => false
      case _ => true
    }
  }

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
    Json.obj("_id" -> id,
      host match {
      case Some(h) => "host" -> Json.obj("_id" -> h._id.stringify, "name" -> h.name)
      case None => "host" -> "null"
      },
      guest match {
        case Some(h) => "guest" -> Json.obj("_id" -> h._id.stringify, "name" -> h.name)
        case None => "guest" -> "null"
      }
    )
    .++(score.toJson)
  }



}
object Match {
  def apply(host:Option[Team], guest:Option[Team], tournamentType: TournamentType) = {
    new Match(0, host, guest, tournamentType.getNewScore())
  }
}
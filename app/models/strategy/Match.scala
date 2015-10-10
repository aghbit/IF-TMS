package models.strategy

import assets.ObjectIdFormat._
import models.Participant
import models.tournament.tournamenttype.TournamentType
import play.api.libs.json.{JsObject, Json}

/**
 * Created by Szymek Seget on 2014-12-02.
 */
class Match (var id:Int,
             var host:Option[Participant],
             var guest:Option[Participant],
             var score:Score) {


  /**
   * Adds team to this match. First check canAddParticipant(), if this return false throw IllegalStateException.
   * Otherwise adds team (passed as argument) to this team. First add as host, if host exists add as guest.
   * @param participant
   */
  def addParticipant(participant: Option[Participant]): Unit = {
    if(!canAddParticipant()){
      throw new IllegalStateException("Can't add participant "+participant.get.getNickName +" to this Match "+id)
    }
    host match {
      case Some(i) => {
        guest match {
          case Some(g) => 
          case None => guest = participant
        }
      }
      case None => host = participant
    }
  }

  def getWinner(): Option[Participant] = {
    if(score.isHostWinner){
      host
    } else if (score.isGuestWinner) {
      guest
    } else {
      None
    }
  }

  def getLoser(): Option[Participant] = {
    if(score.isHostWinner){
      guest
    }else if(score.isGuestWinner){
      host
    }else {
      None
    }
  }

  def canAddParticipant():Boolean = {
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
   *            "name":"participantName"},
   *  "guest": {
   *             "id":BSONObjectID.stringify,
   *             "name":"participantName"},
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
      case Some(h) => "host" -> Json.obj("_id" -> h._id, "name" -> h.getNickName)
      case None => "host" -> None
      },
      guest match {
        case Some(h) => "guest" -> Json.obj("_id" -> h._id, "name" -> h.getNickName)
        case None => "guest" -> None
      }
    )
    .+("score" -> score.toJson)
  }



}
object Match {
  def apply(host:Option[Participant], guest:Option[Participant], tournamentType: TournamentType) = {
    new Match(0, host, guest, tournamentType.getNewScore())
  }
}
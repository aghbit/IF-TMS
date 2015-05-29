package models.tournament.tournamentfields


import models.user.User
import play.api.libs.json.{JsArray, JsObject, Json}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Przemek.
 */
class TournamentStaff(val admin: BSONObjectID,
                      val Referees: java.util.List[BSONObjectID]) {

  def addReferee(newRef: User): Unit = {
    Referees.add(newRef._id)
  }

  def removeReferee(refToRemove: User): Unit = {
    if (!Referees.contains(refToRemove._id))
      throw new NoSuchElementException("Can't remove absent referee from the Tournament!")
    Referees.remove(refToRemove._id)
  }

  def contains(referee: User): Boolean = {
    Referees.contains(referee._id)
  }
  def toJson = {
    Json.obj(
      "admin"->admin.stringify
    )
  }
}

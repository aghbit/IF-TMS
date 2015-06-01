package models.tournament.tournamentfields

import java.util

import models.user.User
import org.bson.types.ObjectId
import play.api.libs.json.{JsArray, JsObject, Json}
import assets.ObjectIdFormat._
/**
 * Created by Przemek.
 */
class TournamentStaff(val admin: ObjectId,
                      var Referees: util.ArrayList[ObjectId]) {

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
      "admin"->admin
    )
  }
}

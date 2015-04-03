package models.tournament.tournamentstate

import java.util

import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Przemek.
 */
class TournamentStaff(val admin: BSONObjectID,
                      var Referees: util.ArrayList[BSONObjectID]) {

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

}

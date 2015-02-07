package models.tournament.tournamentstate

import models.user.User
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek.
 */
class TournamentStaff(val admin: User,
                      var Referees: ListBuffer[BSONObjectID]) {

  def addReferee(newRef: User): Unit = {
    Referees.append(newRef._id)
  }

  def removeReferee(refToRemove: User): Unit = {
    if (!Referees.contains(refToRemove._id))
      throw new NoSuchElementException("Can't remove absent referee from the Tournament!")
    Referees = Referees.filter(id => id != refToRemove._id)
  }

  def contains(referee: User): Boolean = {
    Referees.contains(referee._id)
  }

}

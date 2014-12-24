package models.tournament.tournaments

import models.tournament.tournaments.TournamentDiscipline.Discipline
import models.user.User
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class DuringTournament(override val _id: BSONObjectID,
                       override var properties: TournamentProperties,
                       override var teams: ListBuffer[BSONObjectID],
                       override val discipline: Discipline) extends Tournament{

  override def startNext(): AfterTournament = {
    val newState = new AfterTournament(this._id, this.properties, this.teams, this.discipline)
    newState
  }

  override def addReferee(user: User): Unit = {
    // void
  }

  override def removeReferee(user: User): Unit = {
    // void
  }

  override def editSettings(settings: TournamentSettings): Unit = {
    // void
  }
}

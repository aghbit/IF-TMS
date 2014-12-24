package models.tournament.tournamentfields

import models.tournament.tournamentstate.{TournamentSettings, TournamentProperties, TournamentDiscipline}
import TournamentDiscipline.Discipline
import models.tournament.tournaments.Tournament
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

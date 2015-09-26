package models.tournament.tournamentstates

import java.util

import models.Participant
import models.strategy.EliminationStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties, TournamentSettings}
import models.tournament.tournamenttype.TournamentType
import models.user.User
import org.bson.types.ObjectId

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class DuringTournament(override val _id: ObjectId,
                       override var properties: TournamentProperties,
                       override var participants: util.ArrayList[Participant],
                       override val staff: TournamentStaff,
                       override var strategy: EliminationStrategy,
                       override val discipline: TournamentType) extends Tournament {

  override def startNext(): AfterTournament = {
    val newState = new AfterTournament(this._id, this.properties, this.participants, staff, strategy, discipline)
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

  override def addParticipant(participant: Participant): Unit = {
    throw new IllegalStateException("You can't add participants during this tournament phase")
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit term during this tournament phase")
  }

  override def removeParticipant(participant: Participant): Unit = {
    throw new IllegalStateException("You can't remove participants during this tournament phase")
  }
}

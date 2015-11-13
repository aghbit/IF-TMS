package models.tournament.tournamentstates

import java.util

import models.Participant
import models.strategy.EliminationStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * Created by Przemek
 */
class Enrollment(override val _id: ObjectId,
                 override var properties: TournamentProperties,
                 override var participants: util.ArrayList[Participant],
                 override val staff: TournamentStaff,
                  override var strategy: EliminationStrategy,
                 override val discipline: TournamentType) extends Tournament {

  override def startNext(): Break = {
    val newState = new Break(this._id, this.properties, this.participants, staff, strategy, discipline)
    newState.properties.settings.canEnroll = false
    newState
  }

  override def addParticipant(participant: Participant): Unit = {
    participants.add(participant)
  }

  override def removeParticipant(participant: Participant): Unit = {
    if (!participants.contains(participant))
      throw new NoSuchElementException("Can't remove absent participant from the Tournament!")
    participants.remove(participant)
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit term during this tournament phase")
  }
}
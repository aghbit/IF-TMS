package models.tournament.tournamentstates

import java.util

import models.Participant
import models.strategy.EliminationStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties, TournamentSettings}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * Created by Przemek ..
 */
class Break(override val _id: ObjectId,
            override var properties: TournamentProperties,
            override var participants: util.ArrayList[Participant],
            override val staff: TournamentStaff,
            override var strategy: EliminationStrategy,
            override val discipline: TournamentType) extends Tournament {

  override def startNext(): DuringTournament = {
    val newState = new DuringTournament(this._id, this.properties, this.participants, staff, strategy, discipline)
    newState
  }

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
  }

  override def removeParticipant(participant: Participant): Unit = {
    if (!participants.contains(participant))
      throw new NoSuchElementException("Can't remove absent participant from the Tournament!")
    participants.remove(participant)
  }

  override def addParticipant(participant: Participant): Unit = {
    throw new IllegalStateException("You can't add participants during this tournament phase")
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit terms during this tournament phase")
  }
}

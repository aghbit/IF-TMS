package models.tournament.tournamentfields

import models.strategy.TournamentStrategy
import models.team.Team
import models.tournament.tournaments._
import models.tournament.tournamentstate._
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class BeforeEnrollment(override val _id: BSONObjectID,
                       override var properties: TournamentProperties,
                       override val strategy: TournamentStrategy,
                       override val staff: TournamentStaff) extends Tournament {


  override def startNext(): Enrollment = {
    val newState = new Enrollment(this._id, this.properties, new ListBuffer[BSONObjectID], strategy, staff)
    newState.properties.settings.canEnroll = true
    newState
  }

  override var teams: ListBuffer[BSONObjectID] = _

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
    this.properties.settings.numberOfTeams = settings.numberOfTeams
    this.properties.settings.level = settings.level
    this.properties.settings.discipline = settings.discipline
  }

  override def editTerm(term: TournamentTerm) {
    this.properties.term = term
  }

  override def editDescription(description: TournamentDescription): Unit = {
    this.properties.description = description
  }

  override def addTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't add teams during this tournament phase")
  }

  override def removeTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't remove teams during this tournament phase")
  }
}

object BeforeEnrollment {
  def apply(properties: TournamentProperties, strategy: TournamentStrategy, staff: TournamentStaff): Tournament = {
    val newTournament = new BeforeEnrollment(BSONObjectID.generate, properties, strategy, staff)
    newTournament.properties.settings.canEnroll = false
    newTournament
  }
}


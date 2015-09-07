package models.tournament.tournamentstates

import java.util

import models.strategy.EliminationStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields._
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId


/**
 * Created by Przemek
 */
class BeforeEnrollment(override val _id: ObjectId,
                       override var properties: TournamentProperties,
                       override val staff: TournamentStaff,
                       override var strategy: EliminationStrategy,
                       override val discipline: TournamentType) extends Tournament {


  override def startNext(): Enrollment = {
    val newState = new Enrollment(this._id,
      this.properties,
      new util.ArrayList[Team],
      staff,
      strategy,
      discipline)
    newState.properties.settings.canEnroll = true
    newState
  }

  override var teams: util.ArrayList[Team] = new util.ArrayList[Team]()

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
    this.properties.settings.numberOfTeams = settings.numberOfTeams
    this.properties.settings.level = settings.level
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
  def apply(properties: TournamentProperties,
            staff: TournamentStaff,
            strategy: EliminationStrategy,
             discipline: TournamentType): Tournament = {
    val newTournament = new BeforeEnrollment(ObjectId.get(),
      properties,
      staff,
      strategy, discipline)
    newTournament.properties.settings.canEnroll = false
    newTournament
  }
}


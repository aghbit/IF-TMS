package models.tournament.tournamentstates

import java.util

import models.strategy.{EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields._
import reactivemongo.bson.BSONObjectID


/**
 * Created by Przemek
 */
class BeforeEnrollment(override val _id: BSONObjectID,
                       override var properties: TournamentProperties,
                       override val tree: EliminationTree,
                       override val staff: TournamentStaff) extends Tournament {


  override def startNext(): Enrollment = {
    val newState = new Enrollment(this._id, this.properties, new util.ArrayList[BSONObjectID], tree, staff)
    newState.properties.settings.canEnroll = true
    newState
  }

  override var teams: util.ArrayList[BSONObjectID] = _

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
  def apply(properties: TournamentProperties, tree: EliminationTree, staff: TournamentStaff): Tournament = {
    val newTournament = new BeforeEnrollment(BSONObjectID.generate, properties, tree, staff)
    newTournament.properties.settings.canEnroll = false
    newTournament
  }
}


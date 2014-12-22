package models.tournament.tournaments

import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
class BeforeEnrollment(override val _id: BSONObjectID,
                       override var properties: TournamentProperties,
                       override val discipline: TournamentDiscipline) extends Tournament{


  override var teams: ListBuffer[BSONObjectID] = _

  override def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.level = settings.level
    this.properties.settings.numberOfPitches = settings.numberOfPitches
    this.properties.settings.numberOfTeams = settings.numberOfTeams
  }

  override def editTerm(term: TournamentTerm) {
    this.properties.term = term
  }

  override def editDescription(description: TournamentDescription): Unit = {
    this.properties.description = description
  }
}

object BeforeEnrollment {
  def apply(properties: TournamentProperties, discipline: TournamentDiscipline): Tournament = {
    val newTournament = new BeforeEnrollment(BSONObjectID.generate, properties, discipline)
    newTournament.properties.settings.canEnroll = false
    newTournament
  }
}


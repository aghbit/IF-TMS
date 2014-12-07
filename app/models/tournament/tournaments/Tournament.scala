package models.tournament.tournaments

import models.team.Team
import models.tournament.tournaments.TournamentProperties
import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 * Edited by: Przemek
 */


class Tournament(val _id: Option[BSONObjectID],
                 var properties: TournamentProperties,
                 var teams: List[Team]) {

  def generateTree() = {
    properties.strategy.getOrder()
  }

  def startBeforeEnrollment() = {
    properties.state = TournamentState.BeforeEnrollment
  }

  def startEnrollment() = {
    properties.state = TournamentState.Enrollment
    canEdit(0) = false // description.name
    canEdit(1) = false // description.place
    canEdit(3) = false // term
    canEdit(7) = false // settings.level
  }

  def startBreak() = {
    properties.state = TournamentState.Break
    canEdit(6) = false // settings.canEnroll
    canEdit(5) = false // settings.numberOfTeams
  }

  def startBeforeTournament() = {
    properties.state = TournamentState.BeforeTournament
  }

  def startTournament() = {
    properties.state = TournamentState.Tournament
    canEdit(4) = false // settings.numberOfPitches
    canEdit(8) = false // staff
  }

  def startAfterTournament() = {
    properties.state = TournamentState.AfterTournament
  }

  def changeTournamentProperties(newProperties: TournamentProperties) = {
    if(properties.description != newProperties.description) {
      if (canEdit(0)) properties.description.name = newProperties.description.name
      if (canEdit(1)) properties.description.place = newProperties.description.place
      if (canEdit(2)) properties.description.description = newProperties.description.description
    }

    if(properties.term != newProperties.term) {
      if (canEdit(3)) {
        properties.term.enrollDeadline = newProperties.term.enrollDeadline
        properties.term.begin = newProperties.term.begin
        properties.term.end = newProperties.term.end
        properties.term.extraBegin = newProperties.term.extraBegin
        properties.term.extraEnd = newProperties.term.extraEnd
      }
    }
    if(properties.settings != newProperties.settings) {
      if (canEdit(4)) properties.settings.numberOfPitches = newProperties.settings.numberOfPitches
      if (canEdit(5)) properties.settings.numberOfTeams = newProperties.settings.numberOfTeams
      if (canEdit(6)) properties.settings.canEnroll = newProperties.settings.canEnroll
      if (canEdit(7)) properties.settings.level = newProperties.settings.level
    }
    if(properties.staff != newProperties.staff)
      if(canEdit(8)) properties.staff.Referees = newProperties.staff.Referees
  }

  // canEdit consist of:
  // canEditDescription,  3 fields; 1 - name, 2 - place, 3 - description
  // canEditTerm,         1 fields  1 - all,
  // canEditSettings,     4 fields, 1 - num of pitches, 2 - num of teams, 3 - can enroll, 4 - level
  // canEditStaff,        1 field,  1 - referee because you can't edit admin
  private val canEdit: Array[Boolean] = new Array[Boolean](9)
}

object Tournament {
  def apply(id: Option[BSONObjectID], properties: TournamentProperties, teams: List[Team]) = {
    val newTournament = new Tournament(id, properties, teams)
    newTournament.teams = List[Team]()
    for( a: Int <- 0 to 9) newTournament.canEdit(a) = true
    newTournament.startBeforeEnrollment()
    newTournament
  }
}
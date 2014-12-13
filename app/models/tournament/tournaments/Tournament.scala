package models.tournament.tournaments

import models.team.Team
import models.tournament.tournaments.tournamentfields._
import models.tournament.tournaments.tournamentstate._
import models.user.User
import reactivemongo.bson.BSONObjectID
import scala.collection.mutable.ListBuffer

/**
 * Created by Szymek.
 * Edited by: Przemek
 */


trait Tournament {

  val _id: BSONObjectID
  var properties: TournamentProperties
  var teams: ListBuffer[BSONObjectID]
  val discipline: TournamentDiscipline

  def generateTree() = {
    properties.strategy.getOrder()
  }

  def startEnrollment(): Enrollment = {
    val newState = new Enrollment(this._id, this.properties, new ListBuffer[BSONObjectID], this.discipline)
    newState.properties.settings.canEnroll = true
    newState
  }

  def startBreak(): Break = {
    val newState = new Break(this._id, this.properties, this.teams, this.discipline)
    newState.properties.settings.canEnroll = false
    newState
  }

  def startTournament(): DuringTournament = {
    val newState = new DuringTournament(this._id, this.properties, this.teams, this.discipline)
    newState
  }

  def startAfterTournament(): AfterTournament = {
    val newState = new AfterTournament(this._id, this.properties, this.teams, this.discipline)
    newState
  }

  def addReferee(user:User): Unit = {
    properties.staff.Referees.append(user)
  }

  def addTeam(team: Team): Unit = {
    // void
  }

  def removeTeam(team: Team): Unit = {
    // void
  }

  def removeReferee(user:User): Unit = {
    properties.staff.Referees = properties.staff.Referees.filter(x => x != user)
  }

  def editTerm(term: TournamentTerm): Unit = {
    /* you can edit term only before enrollment so I implemented it there */
  }

  def editSettings(settings: TournamentSettings): Unit = {
    this.properties.settings.numberOfPitches = settings.numberOfPitches
    this.properties.settings.numberOfTeams = settings.numberOfTeams
  }

  def editDescription(description: TournamentDescription): Unit = {
    this.properties.description.description = description.description
  }

  def containsTeam(team: Team):Boolean = {
    teams.contains(team._id)
  }

}
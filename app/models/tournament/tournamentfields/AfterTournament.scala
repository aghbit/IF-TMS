package models.tournament.tournamentfields

import models.team.Team
import models.tournament.tournaments.Tournament
import models.tournament.tournamentstate.{TournamentTerm, TournamentProperties, TournamentSettings}
import models.user.User
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer


/**
 * Created by Przemek
 */
class AfterTournament(override val _id: BSONObjectID,
                      override var properties: TournamentProperties,
                      override var teams: ListBuffer[BSONObjectID]) extends Tournament {
  override def addReferee(user: User): Unit = {
    throw new IllegalStateException("Can't add referee during this tournament state")
  }

  override def removeReferee(user: User): Unit = {
    throw new IllegalStateException("Can't remove referee during this tournament state")
  }

  override def editSettings(settings: TournamentSettings): Unit = {
    throw new IllegalStateException("You can't edit settings during this tournament state")
  }

  override def startNext(): Tournament = {
    throw new IllegalStateException("Can't start next phase of tournament")
  }

  override def addTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't add teams during this tournament phase")
  }

  override def editTerm(term: TournamentTerm): Unit = {
    throw new IllegalStateException("You can't edit term during this tournament phase")
  }

  override def removeTeam(team: Team): Unit = {
    throw new IllegalStateException("You can't remove teams during this tournament phase")
  }
}
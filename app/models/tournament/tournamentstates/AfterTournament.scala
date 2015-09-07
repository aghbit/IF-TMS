package models.tournament.tournamentstates

import java.util

import models.strategy.EliminationStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields.{TournamentStaff, TournamentTerm, TournamentProperties, TournamentSettings}
import models.tournament.tournamenttype.TournamentType
import models.user.User
import org.bson.types.ObjectId



/**
 * Created by Przemek
 */
class AfterTournament(override val _id: ObjectId,
                      override var properties: TournamentProperties,
                      override var teams: util.ArrayList[Team],
                      override val staff: TournamentStaff,
                      override var strategy: EliminationStrategy,
                      override val discipline: TournamentType) extends Tournament {

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

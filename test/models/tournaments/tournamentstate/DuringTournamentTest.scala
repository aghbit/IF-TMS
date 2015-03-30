package models.tournaments.tournamentstate

import models.strategy.TournamentStrategy
import models.team.Team
import models.tournament.tournamentfields.{AfterTournament, BeforeEnrollment}
import models.tournament.tournaments._
import models.tournament.tournamentstate.{TournamentStaff, TournamentProperties, TournamentSettings}
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
@RunWith(classOf[JUnitRunner])
class DuringTournamentTest extends FunSuite with MockitoSugar with BeforeAndAfter {
  var instance: Tournament = _
  var properties: TournamentProperties = _
  var teams: ListBuffer[Team] = _

  before {
    teams = ListBuffer(mock[Team], mock[Team], mock[Team], mock[Team], mock[Team], mock[Team])
    teams.foreach(team => Mockito.when(team._id).thenReturn(BSONObjectID.generate))

    val tournamentPropertiesMock = mock[TournamentProperties]
    val tournamentSettingsMock = mock[TournamentSettings]
    val tournamentStrategyMock = mock[TournamentStrategy]
    val tournamentStaffMock = mock[TournamentStaff]

    Mockito.when(tournamentSettingsMock.canEnroll).thenReturn(false)
    Mockito.when(tournamentPropertiesMock.settings).thenReturn(tournamentSettingsMock)

    instance = BeforeEnrollment(tournamentPropertiesMock, tournamentStrategyMock, tournamentStaffMock)
    instance = instance.startNext().startNext().startNext()
  }

  test("Change tournament state test; (from DuringTournament to AfterTournament)") {
    //given
    instance = instance.startNext()

    //when
    val isReturnTypeValid = instance.isInstanceOf[AfterTournament]

    //then
    assert(isReturnTypeValid)
  }

}
package models.tournaments.tournamentstate

import models.strategy.EliminationStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentstates.{BeforeEnrollment, Enrollment}
import models.tournament.tournamentfields.{TournamentStaff, TournamentProperties, TournamentSettings}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
@RunWith(classOf[JUnitRunner])
class BeforeEnrollmentTest extends FunSuite with MockitoSugar with BeforeAndAfter {
  var instance: Tournament = _
  var properties: TournamentProperties = _
  var teams: ListBuffer[Team] = _

  before {
    teams = ListBuffer(mock[Team], mock[Team], mock[Team], mock[Team], mock[Team], mock[Team])
    teams.foreach(team => Mockito.when(team._id).thenReturn(ObjectId.get))

    val tournamentPropertiesMock = mock[TournamentProperties]
    val tournamentSettingsMock = mock[TournamentSettings]
    val tournamentStaffMock = mock[TournamentStaff]
    val strategyMock = mock[EliminationStrategy]
    val disciplineMock = mock[TournamentType]

    Mockito.when(tournamentSettingsMock.canEnroll).thenReturn(false)
    Mockito.when(tournamentPropertiesMock.settings).thenReturn(tournamentSettingsMock)

    instance = BeforeEnrollment(tournamentPropertiesMock, tournamentStaffMock, strategyMock, disciplineMock)
  }

  test("Change tournament state test; (from BeforeEnrollment to Enrollment)") {
    //given
    instance = instance.startNext()

    //when
    val isReturnTypeValid = instance.isInstanceOf[Enrollment]

    //then
    assert(isReturnTypeValid)
  }

}

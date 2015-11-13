package models.tournaments.tournamentstate

import models.strategy.EliminationStrategy
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentstates.{BeforeEnrollment, Break}
import models.tournament.tournamentfields.{TournamentProperties, TournamentSettings, TournamentStaff}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.bson.types.ObjectId

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
@RunWith(classOf[JUnitRunner])
class EnrollmentTest extends FunSuite with MockitoSugar with BeforeAndAfter {
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
    instance = instance.startNext()
  }

  test("addParticipant test") {
    // given
    instance.addParticipant(teams(0))

    // when
    val containsTeam = instance.containsTeam(teams(0))

    // then
    assert(containsTeam, "addParticipant: test")

  }

  test("removeParticipant: test") {
    //given
    instance.addParticipant(teams(3))
    instance.removeParticipant(teams(3))

    //when
    val containsTeam = instance.containsTeam(teams(3))

    //then
    assert(!containsTeam, "removeParticipant: test")

  }

  test("Change tournament state test; (from Enrollment to Break)") {
    //given
    instance = instance.startNext()

    //when
    val isReturnTypeValid = instance.isInstanceOf[Break]

    //then
    assert(isReturnTypeValid)
  }

}

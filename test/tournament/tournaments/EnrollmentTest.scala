package tournament.tournaments

import models.team.Team
import models.tournament.tournaments.Tournament
import models.tournament.tournaments.tournamentfields.{TournamentSettings, TournamentProperties, TournamentDiscipline}
import models.tournament.tournaments.tournamentstate.BeforeEnrollment
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek_ on 2014-12-13.
 */
@RunWith(classOf[JUnitRunner])
class EnrollmentTest extends FunSuite with MockitoSugar with BeforeAndAfter {
  var instance: Tournament = _
  var properties: TournamentProperties = _
  var teams: ListBuffer[Team] = _
  var discipline: TournamentDiscipline = _

  before {
    teams = ListBuffer(mock[Team], mock[Team], mock[Team], mock[Team], mock[Team], mock[Team])
    discipline = mock[TournamentDiscipline]
    teams.foreach(team => Mockito.when(team._id).thenReturn(BSONObjectID.generate))
    val tournamentPropertiesMock = mock[TournamentProperties]
    val tournamentSettingsMock = mock[TournamentSettings]
    Mockito.when(tournamentSettingsMock.canEnroll).thenReturn(false)
    Mockito.when(tournamentPropertiesMock.settings).thenReturn(tournamentSettingsMock)
    instance = BeforeEnrollment(tournamentPropertiesMock, discipline)
    instance = instance.startEnrollment()
  }

  test("addTeam test") {

    // given
    instance.addTeam(teams(0))

    // when
    val containsTeam = instance.containsTeam(teams(0))

    // then
    assert(containsTeam, "addTeam: test 1")

  }

  test("removeTeam: Simple test") {

    //given
      instance.addTeam(teams(3))
      instance.removeTeam(teams(3))

    //when
    val containsTeam = instance.containsTeam(teams(3))

    //then
    assert(!containsTeam, "RemovePlayer: test 1")

  }

}

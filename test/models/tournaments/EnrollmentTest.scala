package models.tournaments

import models.team.Team
import models.tournament.tournamentfields.BeforeEnrollment
import models.tournament.tournaments._
import models.tournament.tournamentstate.{TournamentProperties, TournamentDiscipline, TournamentSettings}
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
@RunWith(classOf[JUnitRunner])
class EnrollmentTest extends FunSuite with MockitoSugar with BeforeAndAfter {
  var instance: Tournament = _
  var properties: TournamentProperties = _
  var teams: ListBuffer[Team] = _
  var discipline: TournamentDiscipline.Discipline = _

  before {
    teams = ListBuffer(mock[Team], mock[Team], mock[Team], mock[Team], mock[Team], mock[Team])
    discipline = mock[TournamentDiscipline.Discipline]
    teams.foreach(team => Mockito.when(team._id).thenReturn(BSONObjectID.generate))
    val tournamentPropertiesMock = mock[TournamentProperties]
    val tournamentSettingsMock = mock[TournamentSettings]
    Mockito.when(tournamentSettingsMock.canEnroll).thenReturn(false)
    Mockito.when(tournamentPropertiesMock.settings).thenReturn(tournamentSettingsMock)
    instance = BeforeEnrollment(tournamentPropertiesMock, discipline)
    instance = instance.startNext()
  }

  test("addTeam test") {

    // given
    instance.addTeam(teams(0))

    // when
    val containsTeam = instance.containsTeam(teams(0))

    // then
    assert(containsTeam, "addTeam: test")

  }

  test("removeTeam: test") {

    //given
      instance.addTeam(teams(3))
      instance.removeTeam(teams(3))

    //when
    val containsTeam = instance.containsTeam(teams(3))

    //then
    assert(!containsTeam, "RemovePlayer: test")

  }

}

package models.team.teams.volleyball.volleyballs

import models.exceptions.TooManyMembersInTeamException
import models.team.Team
import models.user.User
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import reactivemongo.bson.BSONObjectID


/**
 * Created by Szymek.
 */
@RunWith(classOf[JUnitRunner])
class BeachVolleyballTeamTest extends FunSuite with MockitoSugar with BeforeAndAfter{

  var underTest:Team = _
  var players:List[User] = _

  before{
    underTest = BeachVolleyballTeam("underTest")
    players = List(mock[User], mock[User], mock[User])
    players.foreach(player => Mockito.when(player._id).thenReturn(BSONObjectID.generate))
  }

  test("Constructor: Simple test") {

    //given

    //when
    val underTest:Team = new BeachVolleyballTeam(BSONObjectID.generate, "underTest", 2, 1)

    //then
    assert(underTest.name === "underTest", "Constructor: test 1")
    assert(underTest.isComplete === false, "Constructor: test 2")
    assert(underTest.getMembersIDs === List(), "Constructor: test 3")
  }

  test("AddPlayer: Simple test") {

    //given

    //when
    underTest.addPlayer(players(0))
    val containsPlayer = underTest.containsMember(players(0))

    //then
    assert(containsPlayer, "AddPlayer: test 1")
  }

  test("AddPlayer: Too many players"){

    //given
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))

    //then&then
    intercept[TooManyMembersInTeamException]{
      underTest.addPlayer(players(2))
    }
  }

  test("AddBenchWarmer: Beach volleyball team can't has bench warmers!"){

    //given

    //when&then
    intercept[TooManyMembersInTeamException]{
      underTest.addBenchWarmer(players(1))
    }

  }

  test("RemovePlayer: Simple test") {

    //given
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))

    //when
    underTest.removePlayer(players(0))
    val containsPlayer: Boolean = underTest.containsMember(players(0))

    //then
    assert(!containsPlayer, "RemovePlayer: test 1")
  }

  test("RemovePlayer: Remove absent player") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")

    //when&then
    intercept[NoSuchElementException]{
      underTest.removePlayer(players.head)
    }
  }

  test("RemoveBenchWarmer: Remove absent bench warmer") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")

    //when&then
    intercept[NoSuchElementException]{
      underTest.removeBenchWarmer(players.head)
    }
  }

  test("CaptainID: Throw exception, when wasn't set.") {

    //given

    //when&then
    intercept[NullPointerException]{
      underTest.getCaptainID
    }

  }

  test("SetCaptain: Captain has to be a team member!") {

    //given

    //when&then
    intercept[NoSuchElementException]{
      underTest.setCaptain(players(1))
    }

  }

  test("SetCaptain&CaptainID: Set player as captain"){

    //given
    underTest.addPlayer(players(0))

    //when
    underTest.setCaptain(players(0))
    val captainID = underTest.getCaptainID
    //then
    assert(captainID === players(0)._id, "SetCaptain&CaptainID: test 1")
  }

  test("isComplete: BenchWarmers"){

    //given
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))

    //when
    val isComplete = underTest.isComplete

    //then
    assert(isComplete, "isComplete: BenchWarmers")

  }

}

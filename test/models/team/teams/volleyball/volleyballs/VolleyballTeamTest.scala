
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
class VolleyballTeamTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var underTest:Team = _
  var players:List[User] = _
  var benchWarmers:List[User] = _

  before{
    underTest = VolleyballTeam("underTest")
    players = List(mock[User], mock[User], mock[User], mock[User], mock[User], mock[User])
    players.foreach(player => Mockito.when(player._id).thenReturn(BSONObjectID.generate))
    benchWarmers = List(mock[User], mock[User], mock[User], mock[User], mock[User], mock[User])
    benchWarmers.foreach(benchWarmer => Mockito.when(benchWarmer._id).thenReturn(BSONObjectID.generate))
  }

  test("Constructor: Simple test") {

    //given

    //when
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest", 6, 2)

    //then
    assert(underTest.name === "underTest", "Constructor: test 1")
    assert(underTest.isComplete === false, "Constructor: test 2")
    assert(underTest.getMembersIDs === List(), "Constructor: test 3")
  }

  test("AddPlayer: Simple test") {

    //given
    underTest.addPlayer(players(0))

    //when
    val containsPlayer = underTest.containsMember(players(0))

    //then
    assert(containsPlayer, "AddPlayer: test 1")
  }

  test("AddPlayer: Too many players"){

    //given
    players.foreach(player => underTest.addPlayer(player))

    //when&then
    intercept[TooManyMembersInTeamException]{
      underTest.addPlayer(benchWarmers(0))
    }
  }

  test("RemovePlayer: Simple test") {

    //given
    for(i <- 0 until 6) underTest.addPlayer(players(i))
    underTest.removePlayer(players(3))

    //when
    val containsPlayer = underTest.containsMember(players(3))

    //then
    assert(!containsPlayer, "RemovePlayer: test 1")
  }

  test("RemovePlayer: Remove absent player") {

    //given

    //when&then
    intercept[NoSuchElementException]{
      underTest.removePlayer(players.head)
    }
  }

  test("AddBenchWarmer: SimpleTest"){

    //given

    //when
    underTest.addBenchWarmer(benchWarmers(0))
    val containsBenchWarmer = underTest.containsMember(benchWarmers(0))

    //then
    assert(containsBenchWarmer, "AddBenchWarmers: test 1")
  }

  test("AddBenchWarmer: Too many bench warmers") {

    //given
    benchWarmers.foreach(benchWarmer => underTest.addBenchWarmer(benchWarmer))

    //when&then
    intercept[TooManyMembersInTeamException]{
      underTest.addBenchWarmer(players(0))
    }
  }

  test("RemoveBenchWarmer: Simple test"){

    //given
    benchWarmers.foreach(benchWarmer => underTest.addBenchWarmer(benchWarmer))

    //when
    underTest.removeBenchWarmer(benchWarmers(3))
    val containsBenchWarmer = underTest.containsMember(benchWarmers(3))

    //then
    assert(!containsBenchWarmer, "Remove bench warmer: test 1")
  }

  test("RemoveBenchWarmer: Remove absent bench warmer") {

    //given

    //when&then
    intercept[NoSuchElementException]{
      underTest.removeBenchWarmer(benchWarmers(0))
    }
  }

  test("CaptainID: throw exception, when wasn't set.") {

    //given

    //when&then
    intercept[NullPointerException]{
      underTest.getCaptainID
    }

  }

  test("SetCaptain: Captain has to be a team member!"){

    //given

    //when&then
    intercept[NoSuchElementException]{
      underTest.setCaptain(players(0))
    }
  }

  test("SetCaptain&CaptainID: Captain is a player") {

    //given
    underTest.addPlayer(players(1))

    //when
    underTest.setCaptain(players(1))
    val captainID = underTest.getCaptainID

    //then
    assert(captainID === players(1)._id, "SetCaptain&CaptainID: test 1")

  }

  test("SetCaptain&CaptainID: Captain is a bench warmer"){

    //given
    underTest.addBenchWarmer(benchWarmers(0))

    //when
    underTest.setCaptain(benchWarmers(0))
    val captainID = underTest.getCaptainID

    //then
    assert(captainID === benchWarmers(0)._id, "SetCaptain&CaptainID: test 1")
  }

  test("isComplete: BenchWarmers"){

    //given
    players.foreach(user => underTest.addPlayer(user))

    //when
    val isComplete = underTest.isComplete

    //then
    assert(isComplete, "isComplete: BenchWarmers")
  }

}


package models.team.teams

import models.team.Team
import models.user.User
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
@RunWith(classOf[JUnitRunner])
class VolleyballTeamTest extends FunSuite with MockitoSugar {

  def users():List[User] = {
    val players = List(mock[User], mock[User], mock[User], mock[User], mock[User], mock[User], mock[User])
    players.foreach(player => Mockito.when(player._id).thenReturn(BSONObjectID.generate))
    players
  }

  test("Constructor: simple test") {

    //when
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest", 6)

    //then
    assert(underTest.name === "underTest", "Constructor: test 1")
    assert(underTest.isComplete === false, "Constructor: test 2")
    assert(underTest.getUsersIDs === List(), "Constructor: test 3")
  }

  test("AddPlayer: simple test") {

    //given
    val underTest:Team = VolleyballTeam("underTest")
    val players = users()

    //when
    for( i <- 0 until 6) underTest.addPlayer(players(i))

    //then
    assert(underTest.isComplete, "AddPlayer: test 1")
  }

  test("RemovePlayer: simple test") {

    //given
    val underTest:Team = VolleyballTeam("underTest")
    val players = users()
    for(i <- 0 until 6) underTest.addPlayer(players(i))

    //when
    underTest.removePlayer(players(3))

    //then
    assert(!underTest.isComplete, "RemovePlayer: test 1")
  }

  test("SetCaptainAndCaptainID: simple test") {

    //given
    val underTest:Team = VolleyballTeam("underTest")
    val players = users()

    //when&then
    intercept[NullPointerException]{
      underTest.captainID()
    }
    underTest.addPlayer(players(0))

    //when
    underTest.setCaptain(players(2))
    underTest.addPlayer(players(1))

    //then
    assert(underTest.captainID() === players(2)._id, "SetCaptainAndCaptainID: test 1")

    //when
    underTest.setCaptain(players(0))

    //then
    assert(underTest.captainID() === players(0)._id, "SetCaptainAndCaptainID: test 2")

  }

  test("IsCompleteAddPlayerRemovePlayer: BenchWarmers") {

    //given
    val underTest:Team = VolleyballTeam("underTest")
    val players = users()

    //when
    players.foreach(user => underTest.addPlayer(user))

    //then
    assert(underTest.isComplete, "IsComplete: BenchWarmers")
    assert(underTest.getUsersIDs.length === 7, "AddPlayer: BenchWarmers")

    //when
    underTest.removePlayer(players.head)
    underTest.removePlayer(players.tail.head)

    //then
    assert(!underTest.isComplete, "DeleteUser: BenchWarmers")
  }

  test("RemovePlayer: remove absent player") {

    //given
    val underTest:Team = VolleyballTeam("underTest")

    //when&then
    intercept[NoSuchElementException]{
      underTest.removePlayer(users().head)
    }
  }

}

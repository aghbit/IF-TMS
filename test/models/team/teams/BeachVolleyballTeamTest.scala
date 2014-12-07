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
class BeachVolleyballTeamTest extends FunSuite with MockitoSugar{

  def users():List[User] = {
    val list = List(mock[User], mock[User], mock[User])
    list.foreach(u => Mockito.when(u._id).thenReturn(BSONObjectID.generate))
    list
  }

  test("Constructor: simple test") {

    //when
    val underTest:Team = new BeachVolleyballTeam(BSONObjectID.generate, "underTest", 2)

    //then
    assert(underTest.name === "underTest", "Constructor: test 1")
    assert(underTest.isComplete === false, "Constructor: test 2")
    assert(underTest.getUsersIDs === List(), "Constructor: test 3")
  }

  test("AddPlayer: simple test") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")
    val players = users()

    //when
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))

    //then
    assert(underTest.isComplete, "AddPlayer: test 1")
  }

  test("RemovePlayer: simple test") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")
    val players = users()
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))

    //when
    underTest.removePlayer(players(0))

    //then
    assert(!underTest.isComplete, "RemovePlayer: test 1")
  }

  test("CaptainID: throw exception, when wasn't set.") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")

    //when&then
    intercept[NullPointerException]{
      underTest.captainID()
    }

  }

  test("SetCaptain&CaptainID: simple test") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")
    val players = users()
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(2))

    //when
    underTest.setCaptain(players(1))

    //then
    assert(underTest.captainID() === players(1)._id, "SetCaptain&CaptainID: test 1")

  }

  test("AddPlayer: BenchWarmers") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")
    val players = users()

    //when
    players.foreach(user => underTest.addPlayer(user))

    //then
    assert(underTest.getUsersIDs.length === 3, "AddPlayer: BenchWarmers")
  }

  test("RemovePlayer: BenchWarmers"){

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")
    val players = users()
    players.foreach(user => underTest.addPlayer(user))

    //when
    underTest.removePlayer(players.head)
    underTest.removePlayer(players.tail.head)

    //then
    assert(underTest.getUsersIDs.length === 1, "RemoveUser: BenchWarmers")
  }

  test("isComplete: BenchWarmers"){

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")
    val players = users()
    players.foreach(user => underTest.addPlayer(user))

    //when
    val isComplete = underTest.isComplete

    //then
    assert(isComplete, "isComplete: BenchWarmers")

  }

  test("RemovePlayer: remove absent player") {

    //given
    val underTest:Team = BeachVolleyballTeam("underTest")

    //when&then
    intercept[NoSuchElementException]{
      underTest.removePlayer(users().head)
    }
  }
}

package models.team.teams.volleyball.volleyballs

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


  test("Constructor: simple test") {

    //given

    //when
    val underTest:Team = new BeachVolleyballTeam(BSONObjectID.generate, "underTest", 2)

    //then
    assert(underTest.name === "underTest", "Constructor: test 1")
    assert(underTest.isComplete === false, "Constructor: test 2")
    assert(underTest.getUsersIDs === List(), "Constructor: test 3")
  }

  test("AddPlayer: simple test") {

    //given
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))

    //when
    val isComplete = underTest.isComplete

    //then
    assert(isComplete, "AddPlayer: test 1")
  }

  test("RemovePlayer: simple test") {

    //given
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))
    underTest.removePlayer(players(0))

    //when
    val isComplete = underTest.isComplete

    //then
    assert(!isComplete, "RemovePlayer: test 1")
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
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(2))

    //when
    underTest.setCaptain(players(1))
    val captainID = underTest.captainID()

    //then
    assert(captainID === players(1)._id, "SetCaptain&CaptainID: test 1")

  }

  test("AddPlayer: BenchWarmers") {

    //given
    players.foreach(user => underTest.addPlayer(user))

    //when
    val playersNumber = underTest.getUsersIDs.length

    //then
    assert(playersNumber === 3, "AddPlayer: BenchWarmers")
  }

  test("RemovePlayer: BenchWarmers"){

    //given
    players.foreach(user => underTest.addPlayer(user))
    underTest.removePlayer(players.head)
    underTest.removePlayer(players.tail.head)

    //when
    val playersNumber = underTest.getUsersIDs.length

    //then
    assert(playersNumber === 1, "RemoveUser: BenchWarmers")
  }

  test("isComplete: BenchWarmers"){

    //given
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
      underTest.removePlayer(players.head)
    }
  }
}

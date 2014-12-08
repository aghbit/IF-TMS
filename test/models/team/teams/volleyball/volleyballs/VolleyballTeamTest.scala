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
class VolleyballTeamTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var underTest:Team = _
  var players:List[User] = _

  before{
    underTest = VolleyballTeam("underTest")
    players = List(mock[User], mock[User], mock[User], mock[User], mock[User], mock[User], mock[User])
    players.foreach(player => Mockito.when(player._id).thenReturn(BSONObjectID.generate))
  }

  test("Constructor: simple test") {

    //given

    //when
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest", 6)

    //then
    assert(underTest.name === "underTest", "Constructor: test 1")
    assert(underTest.isComplete === false, "Constructor: test 2")
    assert(underTest.getUsersIDs === List(), "Constructor: test 3")
  }

  test("AddPlayer: simple test") {

    //given
    for( i <- 0 until 6) underTest.addPlayer(players(i))

    //when
    val isComplete:Boolean = underTest.isComplete

    //then
    assert(isComplete, "AddPlayer: test 1")
  }

  test("RemovePlayer: simple test") {

    //given
    for(i <- 0 until 6) underTest.addPlayer(players(i))
    underTest.removePlayer(players(3))

    //when
    val isComplete:Boolean = underTest.isComplete

    //then
    assert(!isComplete, "RemovePlayer: test 1")
  }

  test("CaptainID: throw exception, when wasn't set.") {

    //given

    //when&then
    intercept[NullPointerException]{
      underTest.captainID()
    }

  }

  test("SetCaptain&CaptainID: simple test") {

    //given
    underTest.addPlayer(players(0))
    underTest.addPlayer(players(1))
    underTest.setCaptain(players(2))

    //when
    val captainID = underTest.captainID()

    //then
    assert(captainID === players(2)._id, "SetCaptain&CaptainID: test 1")

  }

  test("AddPlayer: BenchWarmers") {

    //given
    players.foreach(user => underTest.addPlayer(user))

    //when
    val playersNumber = underTest.getUsersIDs.length

    //then
    assert(playersNumber === 7, "AddPlayer: BenchWarmers")

  }

  test("RemovePlayer: BenchWarmers") {

    //given
    players.foreach(user => underTest.addPlayer(user))
    underTest.removePlayer(players.head)
    underTest.removePlayer(players.tail.head)

    //when
    val playersNumber = underTest.getUsersIDs.length

    //then
    assert(playersNumber === 5, "RemovePlayer: BenchWarmers")
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

    //when&then
    intercept[NoSuchElementException]{
      underTest.removePlayer(players.head)
    }
  }

}

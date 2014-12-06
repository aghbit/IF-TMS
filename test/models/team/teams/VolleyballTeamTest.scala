package models.team.teams

import models.team.Team
import models.user.User
import models.user.users.{UserProperties, UserImpl}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
@RunWith(classOf[JUnitRunner])
class VolleyballTeamTest extends FunSuite {

  def users():List[User] = {
    List(new UserImpl(BSONObjectID.generate, new UserProperties("John", "82737402", "john@gmail.com"), null),
      new UserImpl(BSONObjectID.generate, new UserProperties("Kate", "8324242", "kate@gmail.com"), null),
      new UserImpl(BSONObjectID.generate, new UserProperties("Joshua", "3476924", "joshua@gmail.com"), null),
      new UserImpl(BSONObjectID.generate, new UserProperties("Julia", "232412341", "julia@gmail.com"), null),
      new UserImpl(BSONObjectID.generate, new UserProperties("Bill", "14313443", "bill@gmail.com"), null),
      new UserImpl(BSONObjectID.generate, new UserProperties("Mou", "13413444", "mou@gmail.com"), null),
      new UserImpl(BSONObjectID.generate, new UserProperties("Anja", "13413444", "anja@gmail.com"), null)
    )
  }

  test("Constructor: simple test") {
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest")
    assert(underTest.name === "underTest", "Constructor: test 1")
    assert(underTest.isComplete === false, "Constructor: test 2")
    assert(underTest.getUsersIDs === List(), "Constructor: test 3")
  }

  test("AddPlayer: simple test") {
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest")
    val players = users()
    for( i <- 0 until 6) underTest.addPlayer(players(i))
    assert(underTest.isComplete, "AddPlayer: test 1")
  }

  test("RemovePlayer: simple test") {
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest")
    val players = users()
    for(i <- 0 until 6) underTest.addPlayer(players(i))
    underTest.removePlayer(players(3))
    assert(!underTest.isComplete, "RemovePlayer: test 1")
  }

  test("SetCaptainAndCaptainID: simple test") {
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest")
    val players = users()
    val captain:User = new UserImpl(BSONObjectID.generate, new UserProperties("Captain", "07009593", "captain@c.com"), null)
    intercept[NullPointerException]{
      underTest.captainID()
    }
    underTest.addPlayer(players(0))
    underTest.setCaptain(captain)
    underTest.addPlayer(players(1))
    assert(underTest.captainID() === captain._id, "SetCaptainAndCaptainID: test 1")
    underTest.setCaptain(players(0))
    assert(underTest.captainID() === players(0)._id, "SetCaptainAndCaptainID: test 2")

  }

  test("IsCompleteAddPlayerRemovePlayer: BenchWarmers") {
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest")
    val players = users()
    players.foreach(user => underTest.addPlayer(user))
    assert(underTest.isComplete, "IsComplete: BenchWarmers")
    assert(underTest.getUsersIDs.length === 7, "AddPlayer: BenchWarmers")
    underTest.removePlayer(players.head)
    underTest.removePlayer(players.tail.head)
    assert(!underTest.isComplete, "DeleteUser: BenchWarmers")
  }

  test("RemovePlayer: remove absent player") {
    val underTest:Team = new VolleyballTeam(BSONObjectID.generate, "underTest")
    intercept[NoSuchElementException]{
      underTest.removePlayer(users().head)
    }
  }

}

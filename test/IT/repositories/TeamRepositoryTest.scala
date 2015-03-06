package IT.repositories

import models.player.players.Captain
import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.user.User
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.springframework.data.mongodb.core.query._
import reactivemongo.bson.BSONObjectID
import repositories.TeamRepository

/**
 * Created by Szymek.
 */
@RunWith(classOf[JUnitRunner])
class TeamRepositoryTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var underTest: TeamRepository = _
  var team: Team = _
  var captain: Captain = _
  before {
    underTest = new TeamRepository()
    team = VolleyballTeam("Czarne ninje")
    captain = mock[Captain]
    Mockito.when(captain._id).thenReturn(BSONObjectID.generate)
  }

  test("Simple test") {

    //given
    team.addPlayer(captain)
    team.setCaptain(captain)
    val query = new Query(Criteria where "name" is "Czarne ninje")

    //when
    underTest.insert(team)
    val teamRestored = underTest.find(query).get(0)

    //then
    assert(teamRestored.getClass == classOf[VolleyballTeam], "Wrong class type!")
    assert(teamRestored.name == "Czarne ninje", "Wrong name!")
    assert(teamRestored.containsMember(captain), "Added player is lost!")

    //when
    underTest.remove(team)
    val teamRestored2 = underTest.find(query)

    assert(teamRestored2.isEmpty, "Remove error!")
  }

  test("insert: Insert not ready team") {

    //given

    //when&then
    intercept[IllegalArgumentException] {
      underTest.insert(team)
    }

  }
}


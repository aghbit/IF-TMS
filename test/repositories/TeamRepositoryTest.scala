package repositories

import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.user.users.userimpl.UserImpl
import org.mockito.Mockito
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.data.mongodb.core.query._
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class TeamRepositoryTest extends FunSuite with MockitoSugar{

  test("Simple test"){

    //given
    val teamRepository = new TeamRepository
    teamRepository.initTest()
    val team = VolleyballTeam("czarne")
    val user = mock[UserImpl]
    Mockito.when(user._id).thenReturn(BSONObjectID.generate)
    team.addPlayer(user)
    team.setCaptain(user)
    val query = new Query(Criteria where "name" is "czarne")

    //when
    teamRepository.insert(team)
    val teamRestored = teamRepository.find(query).get(0)

    //then
    assert(teamRestored.getClass == classOf[VolleyballTeam], "Wrong class type!")
    assert(teamRestored.name == "czarne", "Wrong name!")
    assert(teamRestored.containsMember(user), "Added player is lost!")

    //when
    teamRepository.remove(query)
    val teamRestored2 = teamRepository.find(query)

    assert(teamRestored2.isEmpty, "Deleting is not working!")
  }

}


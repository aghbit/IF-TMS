package repositories

import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController


//Really important import!
import scala.collection.JavaConversions._


/**
 * Created by Szymek.
 */
@RunWith(classOf[JUnitRunner])
class TeamRepositoryTest extends FunSuite{

  test("Simple add, find and remove demonstration"){

    //given
    val repository = new TeamRepository()
    //change normal db to test db.
    repository.initTest()
    val team = VolleyballTeam("Czarne ninje")
    team.setCaptain(UserImpl(new UserProperties("Szymek", "l", "p", "7", ","), None))

    //add example
    repository.insert(team)

    //find example
    val query = new Query(Criteria where ("name") is ("Czarne ninje"))

    //val teamsFromDB = repository.find(query, classOf[VolleyballTeam])
    repository.remove(query, classOf[VolleyballTeam])
    //println(teamFromDB.head.name)


  }

}

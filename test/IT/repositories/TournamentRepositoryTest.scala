package IT.repositories

import java.util

import com.mongodb.BasicDBObject
import models.strategy.EliminationTree
import models.strategy.eliminationtrees.DoubleEliminationTree
import models.strategy.strategies.{DoubleEliminationStrategy, SingleEliminationStrategy}
import models.tournament.Tournament
import models.tournament.tournamentstates.BeforeEnrollment
import models.tournament.tournamentfields._
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import play.api.Logger
import play.api.libs.json.Json
import org.bson.types.ObjectId
import repositories.TournamentRepository
import org.joda.time.DateTime
import org.springframework.data.mongodb.core.query._
import models.tournament.tournamentfields.JsonFormatTournamentProperties._


/**
 * Created by Szymek.
 */
class TournamentRepositoryTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var underTest:TournamentRepository = _
  var tournament:Tournament = _
  val tournamentDescription = new TournamentDescription("TournamentName", "TournamentPlace", "TournamentDescription")
  val tournamentTerms = new TournamentTerm(new DateTime(2014, 3, 3, 0, 0, 0, 0),
    new DateTime(2014, 3, 20, 8, 0, 0, 0),
    new DateTime(2014, 3, 20, 16, 0, 0, 0),
    new DateTime(2014, 3, 27, 8, 0, 0, 0),
    new DateTime(2014, 3, 27, 16, 0, 0, 0))
  val tournamentSettings = new TournamentSettings(2, 16, true, 1, "Volleyball")
  val tournamentStaff = new TournamentStaff(ObjectId.get(), new util.ArrayList[ObjectId]())
  val tournamentProperties = new TournamentProperties(tournamentDescription, tournamentTerms, tournamentSettings)
  before {
    underTest = new TournamentRepository()
    tournament = BeforeEnrollment(tournamentProperties, tournamentStaff, DoubleEliminationStrategy)
  }

  after {
    underTest.dropCollection()
  }

  test("Simple test"){

    //given
    val query = new BasicDBObject("_id", tournament._id)

    //when
    underTest.insert(tournament)
    val t = underTest.findOne(query)
    val tournamentRestored = t.get

    //then
    assert(tournamentRestored.getClass == classOf[BeforeEnrollment] , "Simple test: Wrong class type!")
    /* uncomment when somebody write equals method for TournamentProperties class. (Przemek)
    assert(tournamentRestored.properties == tournamentProperties, "Simple test: Tournament Properties are wrong!")
    and delete assertion below.
     */
    assert(tournamentRestored.properties.description.name == tournamentProperties.description.name ,
      "Simple test: Tournament Properties are wrong!")
  }
}

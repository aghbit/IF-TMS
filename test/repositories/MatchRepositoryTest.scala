package repositories

import com.mongodb.casbah.commons.ValidBSONType.BSONObject
import com.mongodb.{BasicDBObject, DBObject}
import com.mongodb.casbah.commons.MongoDBObject
import models.player.players.Captain
import models.strategy.Match
import models.strategy.scores.BeachVolleyballScore
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 25.05.15.
 */
class MatchRepositoryTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  var underTest:MatchRepository = _

  before {

    underTest = new MatchRepository
    underTest.dropCollection()
  }

  after {
    //underTest.dropCollection()
  }

  test("Simple test") {
    //given

    //when
    val m = Match(Some(BeachVolleyballTeam("ninje")),
      Some(BeachVolleyballTeam("czad")),
      BeachVolleyball)
    m.score = BeachVolleyballScore()
    m.score.addSet()
    m.score.setScoreInLastSet(21, 19)

    //underTest.insert(m)

    underTest.dropCollection()
    val captain = Captain(ObjectId.get(), "imie", "nazwisko", "7845588969", "pass@dn.pl")
    val host = BeachVolleyballTeam("ninje")
    host.addPlayer(captain)
    host.setCaptain(captain)
    val guest = BeachVolleyballTeam("nico")
    guest.addPlayer(captain)
    guest.setCaptain(captain)
    val teamRepo = new TeamRepository()
    teamRepo.dropCollection()
    teamRepo.insert(host)
    teamRepo.insert(guest)
    val n = Match(Some(host), Some(guest), BeachVolleyball)
    n.score = BeachVolleyballScore()
    n.score.addSet()
    n.score.setScoreInLastSet(21,19)
    n.score.addSet()
    n.score.setScoreInLastSet(19,21)


    underTest.insert(n)
    val u = underTest.findOne(new BasicDBObject())
    //then
  }
}

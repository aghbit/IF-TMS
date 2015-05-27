package repositories

import com.mongodb.{BasicDBObject, DBObject}
import com.mongodb.casbah.commons.MongoDBObject
import models.strategy.Match
import models.strategy.scores.BeachVolleyballScore
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by Szymek Seget on 25.05.15.
 */
class MatchRepositoryTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  var underTest:MatchRepository = _

  before {
    underTest = new MatchRepository

  }

  after {
    underTest.dropCollection()
  }

  test("Simple test") {
    //given

    //when
    val m = Match(Some(BeachVolleyballTeam("ninje")), Some(BeachVolleyballTeam("czad")), BeachVolleyball)
    m.score = BeachVolleyballScore()
    m.score.addSet()
    m.score.setScoreInLastSet(21, 19)

    underTest.insert(m)

    val u = underTest.findOne(new BasicDBObject())
    println(u.getOrElse(""))
    //then
  }
}

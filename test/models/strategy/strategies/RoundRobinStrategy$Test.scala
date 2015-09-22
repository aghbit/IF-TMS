package models.strategy.strategies

import com.mongodb.BasicDBObject
import com.mongodb.casbah.commons.MongoDBObject
import models.player.players.Captain
import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.{Volleyball, BeachVolleyball}
import org.scalatest.FunSuite
import org.bson.types.ObjectId
import repositories.{TeamRepository, EliminationTableRepository}

/**
 * Created by Szymek Seget on 07.09.15.
 */
class RoundRobinStrategy$Test extends FunSuite {

  test("testInitEmpty") {

    //given
    //val underTest = RoundRobinStrategy.initEmpty(new ObjectId(), 10, BeachVolleyball)
    val teams:List[Team] = (for(i <- 1 to 10) yield VolleyballTeam("Team"+i)).toList
    val underTest = RoundRobinStrategy.generate(teams, Volleyball, new ObjectId())

    val teamsRepo = new TeamRepository
    val captain = Captain("nameC", "surnameC", "784588969", "mailC")
    for(t <- teams ;i <- 0 to 4) t.addPlayer(captain)
    for(t<-teams) t.setCaptain(captain)
    teams.foreach(t => teamsRepo.insert(t))
    //when

    val repo = new EliminationTableRepository
    repo.insert(underTest)
    val fromDB = repo.findOne(new BasicDBObject("_id", underTest._id)).get

    assert(fromDB.toString.equals(underTest.toString()))
    //then
  }

}

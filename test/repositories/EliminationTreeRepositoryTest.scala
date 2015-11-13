package repositories

import com.mongodb.BasicDBObject
import models.player.players.Captain
import models.strategy.strategies.DoubleEliminationStrategy
import models.strategy.structures.EliminationTree
import models.team.Team
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.bson.types.ObjectId
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by Szymek Seget on 28.05.15.
 */
class EliminationTreeRepositoryTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  var underTest:EliminationTreeRepository = _
  var tree:EliminationTree = _
  before {
    underTest = new EliminationTreeRepository
    val teams:List[Team] = (for(i <- 0 until 16) yield BeachVolleyballTeam("team "+i)).toList
    val teamsRepo = new TeamRepository
    val captain = Captain(ObjectId.get, "cap", "sur", "784588060", "pas@dm.pl")
    teams.foreach( t => {
      //println("TEAM " + t.name+ " " + t._id.toString)
      t.addPlayer(captain)
      t.setCaptain(captain)
    })
    teams.foreach(t => teamsRepo.insert(t))
    tree = DoubleEliminationStrategy.generate(teams, BeachVolleyball, ObjectId.get())

    val iterator = tree.iterator
    var i = 0
    while(iterator.hasNext && i<10){
      val n = iterator.next()
      //println(n.value.id)
    //  val score = BeachVolleyballScore()
     // score.addPointsContainer()
     // score.setScoreInLastSet(21, 19)
     // score.addPointsContainer()
     // score.setScoreInLastSet(21, 19)
     // n.value.score = score
      DoubleEliminationStrategy.updateMatchResult(tree, n.value)
      i=i+1
    }
    //println(tree.toString())

  }

  after {
    underTest.dropCollection()
  }
  test("Simple"){
    underTest.insert(tree)
    val treee = underTest.findOne(new BasicDBObject())
    //println(treee.get.toString)
  }

}

package repositories

import com.mongodb.BasicDBObject
import com.mongodb.casbah.commons.ValidBSONType.{BSONObject, BasicDBObject}
import models.player.players.Captain
import models.strategy.EliminationTree
import models.strategy.scores.BeachVolleyballScore
import models.strategy.strategies.DoubleEliminationStrategy
import models.team.Team
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.bson.types.ObjectId

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
      println("TEAM " + t.name+ " " + t._id.toString)
      t.addPlayer(captain)
      t.setCaptain(captain)
    })
    teams.foreach(t => teamsRepo.insert(t))
    tree = DoubleEliminationStrategy.generateTree(teams, BeachVolleyball)

    val iterator = tree.iterator
    while(iterator.hasNext){
      val n = iterator.next()
      println(n.value.id)
      val score = BeachVolleyballScore()
      score.addSet()
      score.setScoreInLastSet(21, 19)
      score.addSet()
      score.setScoreInLastSet(19, 21)
      score.addSet()
      score.setScoreInLastSet(15, 13)
      n.value.score = score
      DoubleEliminationStrategy.updateMatchResult(tree, n.value)
    }
    //println(tree.toString())

  }

  test("Simple"){
    //underTest.insert(tree)

   // val treee = underTest.findOne(new BasicDBObject())
   // println("Jesteś zwycięzcą! :D")
   // println(treee.toString)
  }

}

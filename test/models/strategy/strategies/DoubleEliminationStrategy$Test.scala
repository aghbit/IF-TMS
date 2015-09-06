package models.strategy.strategies

import models.strategy.EliminationTree
import models.strategy.eliminationtrees.TreeNode
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.bson.types.ObjectId
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by Szymek Seget on 25.05.15.
 */
class DoubleEliminationStrategy$Test extends FunSuite with BeforeAndAfter with MockitoSugar {

  var eliminationTree:EliminationTree = _

  before {
    val teams = (for (i <- 0 to 15) yield BeachVolleyballTeam("team "+i)).toList
    eliminationTree = DoubleEliminationStrategy.generateTree(teams, BeachVolleyball, ObjectId.get())
  }

  test("Simple test"){
    //println(eliminationTree.toString)
    //println("heheszki")
  }

  test("update process test") {

    //given
    //println(eliminationTree.toString)
    //println("Dupaaa :")
    val iter:Iterator[TreeNode] = eliminationTree.iterator
    while (iter.hasNext){
      val node = iter.next()
      node.value.score.addSet()
      node.value.score.setScoreInLastSet(21,18)
      node.value.score.addSet()
      node.value.score.setScoreInLastSet(21,19)
      DoubleEliminationStrategy.updateMatchResult(eliminationTree, node.value)
      //println("NODEE : " + node.value.id)
      //println(eliminationTree.toString())

    }
    //println("KONIEC!!!!!!!!!!!!!!!!!!!")

    //println(eliminationTree.toString())
    //println(eliminationTree.toJson())

  }

}

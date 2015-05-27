package models.strategy.strategies

import models.strategy.EliminationTree
import models.strategy.eliminationtrees.TreeNode
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by Szymek Seget on 25.05.15.
 */
class DoubleEliminationStrategy$Test extends FunSuite with BeforeAndAfter with MockitoSugar {

  var eliminationTree:EliminationTree = _

  before {
    val teams = (for (i <- 0 to 31) yield BeachVolleyballTeam("team "+i)).toList
    eliminationTree = DoubleEliminationStrategy.generateTree(teams, BeachVolleyball)
  }

  test("Simple test"){
    println(eliminationTree.toString)
    println("heheszki")
  }

  test("update process test") {

    /*//given
    val matchNumber = 7
    println(eliminationTree.toString)
    println("Dupaaa :")
    for (i <- List(12,11,8,7,10,9,6,3,5,4,2,1,0)){
      val node = eliminationTree.getNode(i)
      node.value.score.addSet()
      node.value.score.setScoreInLastSet(21,18)
      node.value.score.addSet()
      node.value.score.setScoreInLastSet(21,19)
      DoubleEliminationStrategy.updateMatchResult(eliminationTree, node.value)
      println(eliminationTree.toString)
    }
    println("KONIEC!!!!!!!!!!!!!!!!!!!")
*/
    println(eliminationTree.toString)

    val iter:Iterator[TreeNode] = eliminationTree.iterator
    while(iter.hasNext) {
      println(iter.next().value.id)
    }

  }

}

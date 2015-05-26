package models.strategy.strategies

import models.strategy.EliminationTree
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
    val teams = (for (i <- 0 to 15) yield BeachVolleyballTeam("team "+i)).toList
    eliminationTree = DoubleEliminationStrategy.generateTree(teams, BeachVolleyball)
  }

  test("Simple test"){
    println(eliminationTree.toString)
    println("heheszki")
  }


}

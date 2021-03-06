package models.strategy.structures.eliminationtrees

import models.strategy.strategies.DoubleEliminationStrategy
import models.strategy.structures.EliminationTree
import models.team.teams.volleyball.volleyballs.BeachVolleyballTeam
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.bson.types.ObjectId
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FunSuite}

/**
 * Created by Szymek Seget on 19.07.15.
 */
class DoubleEliminationTreeTest extends FunSuite with BeforeAndAfter {

  var underTest: EliminationTree = _

  before {
    val teams = (for (i <- 0 to 31) yield BeachVolleyballTeam("team " + i)).toList
    underTest = DoubleEliminationStrategy.generate(teams, BeachVolleyball, ObjectId.get())
  }

  test("Simple test"){
    //println(underTest.toJson().toString())

  }

}

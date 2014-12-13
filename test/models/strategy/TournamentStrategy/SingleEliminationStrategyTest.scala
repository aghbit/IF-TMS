

import models.strategy.TournamentStrategy
import models.strategy.Tree.EliminationTree
import models.strategy.strategies.SingleEliminationStrategy
import models.team.Team
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner

/**
 * Created by ludwik on 13.12.14.
 */
@RunWith(classOf[JUnitRunner])
class SingleEliminationStrategyTest extends FunSuite with BeforeAndAfter with MockitoSugar {
    var underTest: TournamentStrategy = _

  before{
    underTest = new SingleEliminationStrategy(List(mock[Team]))
  }

  test("generateTree: Simple test"){

    //given


    //when

    val tree = underTest.generateTree()
    val condition = tree match  {
      case t:EliminationTree => true
      case _ => false
    }

    //then

    assert(condition, "Generate Tree Simple Test doesnt work.")
  }




}

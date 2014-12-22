package models.statistics.user.volleyballs.volleyball

import models.statistics.user.TournamentsUserStatistics
import models.statistics.user.volleyballs.MatchesAndSetsUserStatistics
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

/**
 * Created by krzysiek.
 */
@RunWith(classOf[JUnitRunner])
class VolleyballUserStatisticsTest extends FunSuite with MockitoSugar with BeforeAndAfter{

  var testInstance:VolleyballUserStatistics = _

  val discipline:String = "Volleyball"
  val pointUnit:String = "point"
  val winStreak = 3

  before{
    testInstance = new VolleyballUserStatistics(discipline,pointUnit,mock[TournamentsUserStatistics],
            mock[MatchesAndSetsUserStatistics],winStreak)
  }

  test("Constructor test"){

    //given

    //when
    val testInstance:VolleyballUserStatistics = new VolleyballUserStatistics(discipline,pointUnit,
            mock[TournamentsUserStatistics],mock[MatchesAndSetsUserStatistics],winStreak)

    //then
    assert(testInstance.discipline === "Volleyball", "Constructor: test discipline")
    assert(testInstance.pointUnit === "point", "Constructor: test pointUnit")
    assert(testInstance.winStreak === 3, "Constructor: test winStreak")
  }

  test("didTeamWin test1 - won"){

    //given
    val win:Boolean = true

    //when
    testInstance.didTeamWin(win)

    //then
    assert(testInstance.winStreak === 4, "winStreak test1")
  }

  test("didTeamWin test2 - lost"){

    //given
    val loss:Boolean = false

    //when
    testInstance.didTeamWin(loss)

    //then
    assert(testInstance.winStreak === 0, "winStreak test2")
  }
}

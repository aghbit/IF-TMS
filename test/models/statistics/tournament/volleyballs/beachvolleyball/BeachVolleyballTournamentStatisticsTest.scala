package models.statistics.tournament.volleyballs.beachvolleyball

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

/**
 * Created by krzysiek.
 */
@RunWith(classOf[JUnitRunner])
class BeachVolleyballTournamentStatisticsTest extends FunSuite with MockitoSugar with BeforeAndAfter{

  var testInstance:BeachVolleyballTournamentStatistics = _

  val discipline:String = "Beach Volleyball"
  val pointUnit:String = "point"
  val numberOfMatches:Int = 12
  val numberOfSets:Int = 36
  val numberOfPoints:Int = 200
  val numberOfTieBreaks:Int = 10

  before{
    testInstance = new BeachVolleyballTournamentStatistics(discipline,pointUnit,numberOfMatches,numberOfSets,
            numberOfPoints,numberOfTieBreaks)
  }

  test("Constructor test"){

    //given

    //when
    val testInstance:BeachVolleyballTournamentStatistics = new BeachVolleyballTournamentStatistics(discipline,pointUnit,
            numberOfMatches,numberOfSets,numberOfPoints,numberOfTieBreaks)

    //then
    assert(testInstance.discipline === "Beach Volleyball", "Constructor: test discipline")
    assert(testInstance.pointUnit === "point", "Constructor: test pointUnit")
    assert(testInstance.numberOfMatches === 12, "Constructor: test numberOfMatches")
    assert(testInstance.numberOfSets === 36, "Constructor: test numberOfSets")
    assert(testInstance.numberOfPoints === 200, "Constructor: test numberOfPoints")
    assert(testInstance.numberOfTieBreaks === 10, "Constructor: test numberOfBreaks")
  }
}

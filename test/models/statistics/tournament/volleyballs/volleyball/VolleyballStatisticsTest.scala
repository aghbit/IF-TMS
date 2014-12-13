package models.statistics.tournament.volleyballs.volleyball

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

/**
 * Created by krzysiek.
 */
@RunWith(classOf[JUnitRunner])
class VolleyballStatisticsTest extends FunSuite with MockitoSugar with BeforeAndAfter{

  var testInstance:VolleyballStatistics = _

  val discipline:String = "Volleyball"
  val pointUnit:String = "point"
  val numberOfMatches:Int = 12
  val numberOfSets:Int = 36
  val numberOfPoints:Int = 200
  val numberOfTieBreaks:Int = 10
  val numberOfSubstitutions:Int = 15

  before{
    testInstance = new VolleyballStatistics(discipline,pointUnit,numberOfMatches,numberOfSets,numberOfPoints,numberOfTieBreaks,numberOfSubstitutions)
  }

  test("Constructor test"){

    //given

    //when
    val testInstance:VolleyballStatistics = new VolleyballStatistics(discipline,pointUnit,numberOfMatches,numberOfSets,numberOfPoints,numberOfTieBreaks,numberOfSubstitutions)

    //then
    assert(testInstance.discipline === "Volleyball", "Constructor: test discipline")
    assert(testInstance.pointUnit === "point", "Constructor: test pointUnit")
    assert(testInstance.numberOfMatches === 12, "Constructor: test numberOfMatches")
    assert(testInstance.numberOfSets === 36, "Constructor: test numberOfSets")
    assert(testInstance.numberOfPoints === 200, "Constructor: test numberOfPoints")
    assert(testInstance.numberOfTieBreaks === 10, "Constructor: test numberOfBreaks")
    assert(testInstance.numberOfSubstitutions === 15, "Constructor: test numberOfSubstitutions")
  }

}

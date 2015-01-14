package models.statistics.user.volleyballs

import models.exceptions.{NegativeValueException, TooManySetsInMatchException}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by krzysiek.
 */
@RunWith(classOf[JUnitRunner])
class MatchesAndSetsUserStatisticsTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var testInstance: MatchesAndSetsUserStatistics = _

  val discipline: String = "Beach Volleyball"
  val numberOfWonMatches: Int = 10
  val numberOfLostMatches: Int = 6
  val numberOfWonSets: Int = 25
  val numberOfLostSets: Int = 15
  val numberOfPoints: Int = 500

  before {
    testInstance = new MatchesAndSetsUserStatistics(discipline, numberOfWonMatches, numberOfLostMatches, numberOfWonSets,
      numberOfLostSets, numberOfPoints)
  }

  test("Constructor test") {

    //given

    //when
    val testInstance: MatchesAndSetsUserStatistics = new MatchesAndSetsUserStatistics(discipline, numberOfWonMatches,
      numberOfLostMatches, numberOfWonSets, numberOfLostSets, numberOfPoints)

    //then
    assert(testInstance.discipline === "Beach Volleyball", "Constructor test discipline")
    assert(testInstance.numberOfWonMatches === 10, "Constructor: test numberOfWonMatches")
    assert(testInstance.numberOfLostMatches === 6, "Constructor: test numberOfLostMatches")
    assert(testInstance.numberOfWonSets === 25, "Constructor: test numberOfSets")
    assert(testInstance.numberOfLostSets === 15, "Constructor: test numberOfSets")
    assert(testInstance.numberOfPoints === 500, "Constructor: test numberOfPoints")
  }

  test("addNumberOfWonMatches test") {

    //given

    //when
    testInstance.addNumberOfWonMatches()

    //then
    assert(testInstance.numberOfWonMatches === 11, "addNumberOfWonMatches test")
  }

  test("addNumberOfLostMatches test") {

    //given

    //when
    testInstance.addNumberOfLostMatches()

    //then
    assert(testInstance.numberOfLostMatches === 7, "addNumberOfLostMatches test")
  }

  test("addNumberOfWonSets test1") {

    //given

    //when
    testInstance.addNumberOfWonSets(2)

    //then
    assert(testInstance.numberOfWonSets === 27, "addNumberOfWonSets test1")
  }

  test("addNumberOfWonSets test2 - too many sets") {

    //given

    //when&then
    intercept[TooManySetsInMatchException] {
      testInstance.addNumberOfWonSets(3)
    }
  }

  test("addNumberOfWonSets test3 - negative value") {

    //given

    //when&then
    intercept[NegativeValueException] {
      testInstance.addNumberOfWonSets(-2)
    }
  }

  test("addNumberOfLostSets test1") {

    //given

    //when
    testInstance.addNumberOfLostSets(2)

    //then
    assert(testInstance.numberOfLostSets === 17, "addNumberOfWonSets test")
  }

  test("addNumberOfLostSets test2 - too many sets") {

    //given

    //when&then
    intercept[TooManySetsInMatchException] {
      testInstance.addNumberOfLostSets(5)
    }
  }

  test("addNumberOfLostSets test3 - negative value") {

    //given

    //when&then
    intercept[NegativeValueException] {
      testInstance.addNumberOfLostSets(-3)
    }
  }

  test("addNumberOfPoints test1") {

    //given

    //when
    testInstance.addNumberOfPoints(30)

    //then
    assert(testInstance.numberOfPoints === 530, "addNumberOfPoints test")
  }

  test("addNumberOfPoints test2 - negative value") {

    //given

    //when&then
    intercept[NegativeValueException] {
      testInstance.addNumberOfPoints(-5)
    }
  }
}
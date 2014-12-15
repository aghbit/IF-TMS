package models.statistics.user.volleyballs

import models.exceptions.{NegativeValueException, TooManySetsInMatchException}
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

/**
 * Created by krzysiek.
 */
@RunWith(classOf[JUnitRunner])
class MatchesAndSetsStatisticsTest extends FunSuite with MockitoSugar with BeforeAndAfter{

  var testInstance:MatchesAndSetsStatistics = _

  val discipline:String = "Beach Volleyball"
  val numberOfWonMatches:Int = 10
  val numberOfLostMatches:Int = 6
  val numberOfWonSets:Int = 25
  val numberOfLostSets:Int = 15

  before{
    testInstance = new MatchesAndSetsStatistics(discipline,
            numberOfWonMatches,numberOfLostMatches,numberOfWonSets,numberOfLostSets)
  }

  test("Constructor test"){

    //given

    //when
    val testInstance:MatchesAndSetsStatistics = new MatchesAndSetsStatistics(discipline,
            numberOfWonMatches,numberOfLostMatches,numberOfWonSets,numberOfLostSets)

    //then
    assert(testInstance.discipline === "Beach Volleyball", "Constructor test discipline")
    assert(testInstance.numberOfWonMatches === 10, "Constructor: test numberOfWonMatches")
    assert(testInstance.numberOfLostMatches === 6, "Constructor: test numberOfLostMatches")
    assert(testInstance.numberOfWonSets === 25, "Constructor: test numberOfSets")
    assert(testInstance.numberOfLostSets === 15, "Constructor: test numberOfSets")
  }

  test("addNumberOfWonMatches test"){

    //given

    //when
    testInstance.addNumberOfWonMatches()

    //then
    assert(testInstance.numberOfWonMatches === 11, "addNumberOfWonMatches test")
  }

  test("addNumberOfLostMatches test"){

    //given

    //when
    testInstance.addNumberOfLostMatches()

    //then
    assert(testInstance.numberOfLostMatches === 7, "addNumberOfLostMatches test")
  }

  test("addNumberOfWonSets test1"){

    //given

    //when
    testInstance.addNumberOfWonSets(3)

    //then
    assert(testInstance.numberOfWonSets === 28, "addNumberOfWonSets test")
  }

  test("addNumberOfWonSets test2 - too many sets"){

    //given

    //when&then
    intercept[TooManySetsInMatchException]{
      testInstance.addNumberOfWonSets(4)
    }
  }

  test("addNumberOfWonSets test3 - negative value"){

    //given

    //when&then
    intercept[NegativeValueException]{
      testInstance.addNumberOfWonSets(-2)
    }
  }

  test("addNumberOfLostSets test1"){

    //given

    //when
    testInstance.addNumberOfLostSets(2)

    //then
    assert(testInstance.numberOfLostSets === 17, "addNumberOfWonSets test")
  }

  test("addNumberOfLostSets test2 - too many sets"){

    //given

    //when&then
    intercept[TooManySetsInMatchException]{
      testInstance.addNumberOfLostSets(5)
    }
  }

  test("addNumberOfLostSets test3 - negative value"){

    //given

    //when&then
    intercept[NegativeValueException]{
      testInstance.addNumberOfLostSets(-3)
    }
  }

}
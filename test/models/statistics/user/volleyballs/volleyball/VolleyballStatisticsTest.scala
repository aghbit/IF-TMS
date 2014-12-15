package models.statistics.user.volleyballs.volleyball

import models.exceptions.NegativeValueException
import models.statistics.user.volleyballs.MatchesAndSetsStatistics
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import reactivemongo.bson.BSONObjectID

/**
 * Created by krzysiek.
 */
@RunWith(classOf[JUnitRunner])
class VolleyballStatisticsTest extends FunSuite with MockitoSugar with BeforeAndAfter{

  var testInstance:VolleyballStatistics = _

  val discipline:String = "Volleyball"
  val pointUnit:String = "point"
  val tournamentsID:Array[BSONObjectID] = Array(BSONObjectID.generate)
  val numberOfTournaments:Int = tournamentsID.length
  val numberOfWonTournaments:Int = 1
  val numberOfPoints:Int = 500
  val winStreak = 3

  before{
    testInstance = new VolleyballStatistics(discipline,pointUnit,tournamentsID,numberOfTournaments,
            numberOfWonTournaments,mock[MatchesAndSetsStatistics],numberOfPoints,winStreak)
  }

  test("Constructor test"){

    //given

    //when
    val testInstance:VolleyballStatistics = new VolleyballStatistics(discipline,pointUnit,tournamentsID,
            numberOfTournaments,numberOfWonTournaments,mock[MatchesAndSetsStatistics],numberOfPoints,winStreak)

    //then
    assert(testInstance.discipline === "Volleyball", "Constructor: test discipline")
    assert(testInstance.pointUnit === "point", "Constructor: test pointUnit")
    assert(testInstance.numberOfTournaments === 1, "Constructor: test numberOfTournaments")
    assert(testInstance.numberOfWonTournaments === 1, "Constructor: test numberOfWonTournaments")
    assert(testInstance.numberOfPoints === 500, "Constructor: test numberOfPoints")
    assert(testInstance.winStreak === 3, "Constructor: test winStreak")
  }

  test("addTournament test"){

    //given

    //when
    testInstance.addTournament(BSONObjectID.generate)

    //then
    assert(testInstance.numberOfTournaments === 2, "addTournament test")
  }

  test("addNumberOfWonTournaments test"){

    //given

    //when
    testInstance.addNumberOfWonTournaments()

    //then
    assert(testInstance.numberOfWonTournaments === 2, "addNumberOfWonTournaments test")
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

  test("addNumberOfPoints test1"){

    //given

    //when
    testInstance.addNumberOfPoints(30)

    //then
    assert(testInstance.numberOfPoints === 530, "addNumberOfPoints test")
  }

  test("addNumberOfPoints test2 - negative value"){

    //given

    //when&then
    intercept[NegativeValueException]{
      testInstance.addNumberOfPoints(-5)
    }
  }

}

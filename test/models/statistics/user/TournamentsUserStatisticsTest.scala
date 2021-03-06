package models.statistics.user

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.bson.types.ObjectId

/**
 * Created by krzysiek.
 */
@RunWith(classOf[JUnitRunner])
class TournamentsUserStatisticsTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var testInstance: TournamentsUserStatistics = _

  val tournamentsID: Array[ObjectId] = Array(ObjectId.get)
  val numberOfTournaments: Int = tournamentsID.length
  val numberOfWonTournaments: Int = 1

  before {
    testInstance = new TournamentsUserStatistics(tournamentsID, numberOfTournaments, numberOfWonTournaments)
  }

  test("Constructor test") {

    //given

    //when
    val testInstance: TournamentsUserStatistics = new TournamentsUserStatistics(tournamentsID, numberOfTournaments,
      numberOfWonTournaments)

    //then
    assert(testInstance.numberOfTournaments === 1, "Constructor: test numberOfTournaments")
    assert(testInstance.numberOfWonTournaments === 1, "Constructor: test numberOfWonTournaments")
  }

  test("addTournament test") {

    //given

    //when
    testInstance.addTournament(ObjectId.get())

    //then
    assert(testInstance.numberOfTournaments === 2, "addTournament test")
  }

  test("addNumberOfWonTournaments test") {

    //given

    //when
    testInstance.addNumberOfWonTournaments()

    //then
    assert(testInstance.numberOfWonTournaments === 2, "addNumberOfWonTournaments test")
  }

}

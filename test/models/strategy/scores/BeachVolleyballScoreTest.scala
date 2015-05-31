package models.strategy.scores

import models.exceptions.MatchFinishedException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import reactivemongo.bson.BSONObjectID


/**
 * Created by Rafal on 2014-12-07.
 */
@RunWith(classOf[JUnitRunner])
class BeachVolleyballScoreTest extends FunSuite with BeforeAndAfter {

  var bvolScore: BeachVolleyballScore = _
  val host = BSONObjectID.generate
  val guest = BSONObjectID.generate

  before {
    bvolScore = BeachVolleyballScore(host, guest)
  }


  test("Primary test") {

    //given

    //when
    val hostSets = bvolScore.hostSets
    val guestSets = bvolScore.hostSets

    //then
    assert(bvolScore.maxPoints === 21, "Primary test1")
    assert(hostSets.size === 1, "Primary test2")
    assert(guestSets.size === 1, "Primary test3")
    assert(!bvolScore.isMatchFinished, "Primary test4")
  }

  test("AddPoint: FirstTest") {

    //given
    for (i <- 0 until 41) bvolScore.addPoint(host)

    //when
    val hostSets = bvolScore.hostSets
    val guestSets = bvolScore.guestSets

    //then
    assert(hostSets.size === 2, "AddPoint: FirstTest1")
    assert(guestSets.size === 2, "AddPoint: FirstTest2")
    assert(!bvolScore.isMatchFinished, "AddPoint: FirstTest3")
    assert(!bvolScore.isMatchFinished, "AddPoint: FirstTest4")
  }

  test("AddPoint: Too many points test") {

    //given
    for (i <- 0 until 42) bvolScore.addPoint(host)

    //when

    //then
    intercept[MatchFinishedException] {
      bvolScore.addPoint(host)
    }
  }


  test("IsEnded: Match is finished test") {

    //given
    for (i <- 0 until 42) bvolScore.addPoint(host)

    //when
    val isEnded = bvolScore.isMatchFinished
    val sizeOfHostSets = bvolScore.hostSets.size
    val sizeOfGuestSets = bvolScore.guestSets.size

    //then
    assert(isEnded, "IsEnded: Match is finished test1")
    assert(sizeOfHostSets === 3, "IsEnded: Match is finished test2")
    assert(sizeOfGuestSets === 3, "IsEnded: Match is finished test3")
  }

  test("IsEnded: 1 pointed advantage test") {

    //given
    for (i <- 0 until 21) bvolScore.addPoint(host)
    for (i <- 0 until 20) {
      bvolScore.addPoint(host)
      bvolScore.addPoint(guest)
    }
    bvolScore.addPoint(host)

    //when
    val isEnded = bvolScore.isMatchFinished

    //then
    assert(!isEnded, "IsEnded: 1 pointed advantage test1")
  }

  test("IsEnded: draw test") {

    //given
    for (i <- 0 until 21) bvolScore.addPoint(host)
    for (i <- 0 until 21) {
      bvolScore.addPoint(host)
      bvolScore.addPoint(guest)
    }
    //when
    val isEnded1 = bvolScore.isMatchFinished
    val isEnded2 = bvolScore.isMatchFinished

    //then
    assert(!isEnded1, "IsEnded: draw test1")
    assert(!isEnded2, "IsEnded: draw test2")

  }

  test("IsEnded: advantage finally made test") {

    //given
    for (i <- 0 until 21) bvolScore.addPoint(host)
    for (i <- 0 until 20) {
      bvolScore.addPoint(host)
      bvolScore.addPoint(guest)
    }
    bvolScore.addPoint(host)
    bvolScore.addPoint(host)



    //when


    //then
    assert(bvolScore.isMatchFinished, "IsEnded: advantage finally made test1")

  }

  test("GetWinnerLoserTest: Simple Test") {

    //given
    for (i <- 0 until 42) bvolScore.addPoint(host)
    //when

    //then
    assert(bvolScore.getWinner == Some(host), "WinnerTest")
    assert(bvolScore.getLoser == Some(guest), "LoserTest")
  }

}

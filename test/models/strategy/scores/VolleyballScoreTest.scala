package models.strategy.scores

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import reactivemongo.bson.BSONObjectID


/**
 * Created by Rafal on 2014-12-07.
 */
@RunWith(classOf[JUnitRunner])
class VolleyballScoreTest extends FunSuite with BeforeAndAfter{

    var volScore:VolleyballScore = _
    val host = BSONObjectID.generate
    val guest = BSONObjectID.generate

    before{
      volScore = VolleyballScore(host,guest)
    }


    test("Primary test") {

      //given

      //when
      val hostSets = volScore.hostSets
      val guestSets = volScore.hostSets

      //then
      assert(volScore.maxPoints === 25, "Primary test1")
      assert(hostSets.size === 1, "Primary test2")
      assert(guestSets.size === 1, "Primary test3")
      assert(!volScore.isMatchFinished, "Primary test4")
    }

    test("AddPoint: FirstTest") {

      //given
      for (i <- 0 until 74) volScore.addPoint(host)

      //when
      val hostSets = volScore.hostSets
      val guestSets = volScore.guestSets

      //then
      assert(hostSets.size === 3, "AddPoint: FirstTest1")
      assert(guestSets .size === 3, "AddPoint: FirstTest2")
      assert(!volScore.isMatchFinished, "AddPoint: FirstTest3")
      assert(!volScore.isMatchFinished, "AddPoint: FirstTest4")
    }

    test("AddPoint: Too many points test") {

      //given
      for (i <- 0 until 75) volScore.addPoint(host)

      //when

      //then
      intercept[MatchFinishedException] {
        volScore.addPoint(host)
      }
    }


    test("IsEnded: Match is finished test"){

      //given
      for (i <- 0 until 75) volScore.addPoint(host)

      //when
      val isEnded = volScore.isMatchFinished
      val sizeOfHostSets = volScore.hostSets.size
      val sizeOfGuestSets = volScore.guestSets.size

      //then
      assert(isEnded, "IsEnded: Match is finished test1")
      assert(sizeOfHostSets === 4, "IsEnded: Match is finished test2")
      assert(sizeOfGuestSets === 4, "IsEnded: Match is finished test3")
    }

    test("IsEnded: 1 pointed advantage test") {

      //given
      for (i <- 0 until 50) volScore.addPoint(host)
      for (i <- 0 until 24) {
        volScore.addPoint(host)
        volScore.addPoint(guest)
      }
      volScore.addPoint(host)

      //when
      val isEnded = volScore.isMatchFinished

      //then
      assert(!isEnded, "IsEnded: 1 pointed advantage test1")
    }

    test("IsEnded: draw test") {

      //given
      for (i <- 0 until 50) volScore.addPoint(host)
      for (i <- 0 until 25) {
        volScore.addPoint(host)
        volScore.addPoint(guest)
      }
      //when
      val isEnded1 = volScore.isMatchFinished
      val isEnded2 = volScore.isMatchFinished

      //then
      assert(!isEnded1, "IsEnded: draw test1")
      assert(!isEnded2, "IsEnded: draw test2")

    }

    test("IsEnded: advantage finally made test"){

      //given
      for (i <- 0 until 50) volScore.addPoint(host)
      for (i <- 0 until 24) {
        volScore.addPoint(host)
        volScore.addPoint(guest)
      }
      volScore.addPoint(host)
      volScore.addPoint(host)



      //when


      //then
      assert(volScore.isMatchFinished, "IsEnded: advantage finally made test1")

    }

    test("GetWinnerLoserTest: Simple Test") {

      //given
      for (i <- 0 until 75) volScore.addPoint(host)
      //when

      //then
      assert(volScore.getWinner==Some(host), "WinnerTest")
      assert(volScore.getLoser==Some(guest), "LoserTest")
    }


}
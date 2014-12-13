package models.strategy.scores

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner


/**
 * Created by Rafal on 2014-12-07.
 */
@RunWith(classOf[JUnitRunner])
class VolleyballScoreTest extends FunSuite with BeforeAndAfter{

    var bvolScore1:VolleyballScore = _
    var bvolScore2:VolleyballScore = _

    before{
      bvolScore1 = VolleyballScore()
      bvolScore2 = VolleyballScore()
    }


    test("Primary test") {

      //given

      //when
      val sets = bvolScore1.sets

      //then
      assert(bvolScore1.maxPoints === 25, "Primary test1")
      assert(sets.size === 1, "Primary test2")
      assert(!bvolScore1.isEnded, "Primary test3")
    }

    test("AddPoint: FirstTest") {

      //given
      for (i <- 0 until 74) bvolScore1.addPoint(bvolScore2)

      //when
      val sets1 = bvolScore1.sets
      val sets2 = bvolScore2.sets

      //then
      assert(sets1.size === 3, "AddPoint: FirstTest1")
      assert(sets2.size === 3, "AddPoint: FirstTest2")
      assert(!bvolScore1.isEnded, "AddPoint: FirstTest3")
      assert(!bvolScore2.isEnded, "AddPoint: FirstTest4")
    }

    test("IsEnded: Match is finished test"){

      //given
      for (i <- 0 until 75) bvolScore1.addPoint(bvolScore2)

      //when
      val isEnded = bvolScore1.isEnded
      val sizeOfSets1 = bvolScore1.sets.size
      val sizeOfSets2 = bvolScore2.sets.size

      //then
      assert(isEnded, "IsEnded: Match is finished test1")
      assert(sizeOfSets1 === 4, "IsEnded: Match is finished test2")
      assert(sizeOfSets2 === 4, "IsEnded: Match is finished test3")
    }

    test("IsEnded: 1 pointed advantage test") {

      //given
      for (i <- 0 until 50) bvolScore2.addPoint(bvolScore1)
      for (i <- 0 until 24) {
        bvolScore1.addPoint(bvolScore2)
        bvolScore2.addPoint(bvolScore1)
      }
      bvolScore2.addPoint(bvolScore1)

      //when
      val isEnded = bvolScore2.isEnded

      //then
      assert(!isEnded, "IsEnded: 1 pointed advantage test1")
    }

    test("IsEnded: draw test") {

      //given
      for (i <- 0 until 50) bvolScore2.addPoint(bvolScore1)
      for (i <- 0 until 25) {
        bvolScore1.addPoint(bvolScore2)
        bvolScore2.addPoint(bvolScore1)
      }

      //when
      val isEnded1 = bvolScore1.isEnded
      val isEnded2 = bvolScore2.isEnded

      //then
      assert(!isEnded1, "IsEnded: draw test1")
      assert(!isEnded2, "IsEnded: draw test2")

    }

    test("IsEnded: advantage finally made test"){

      //given
      for (i <- 0 until 50) bvolScore2.addPoint(bvolScore1)
      for (i <- 0 until 24) {
        bvolScore1.addPoint(bvolScore2)
        bvolScore2.addPoint(bvolScore1)
      }
      bvolScore2.addPoint(bvolScore1)
      bvolScore2.addPoint(bvolScore1)

      bvolScore2.sets.foreach(vset => println(vset.points+" "+vset.won))
      bvolScore1.sets.foreach(vset => println(vset.points+" "+vset.won))
      //when


      //then
      assert(bvolScore2.isEnded, "IsEnded: advantage finally made test1")

    }

    test("WinnerTest: Simple Test") {

      //given
      for (i <- 0 until 75) bvolScore2.addPoint(bvolScore1)
      //when

      //then
      assert(!(bvolScore1>bvolScore2), "WinnerTest1")
      assert(bvolScore1<bvolScore2, "WinnerTest2")
    }

    test("WinnerTest: match is not finished"){

      //given

      //when&then
      intercept[MatchNotFinishedException] {
        bvolScore2>bvolScore1
      }
      intercept[MatchNotFinishedException] {
        bvolScore2<bvolScore1
      }
    }
}
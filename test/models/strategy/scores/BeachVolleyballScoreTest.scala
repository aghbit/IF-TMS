package models.strategy.scores

import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import play.api.libs.json.Json

/**
 * Created by Szymek Seget on 24.05.15.
 */
class BeachVolleyballScoreTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  var underTest:BeachVolleyballScore = _

  before {
    underTest = BeachVolleyballScore()
  }

  test("Simple test"){

    //given

    //when
    underTest = underTest.firstSetHostScore(21)
                         .firstSetGuestScore(19)
                         .secondSetHostScore(22)
                         .secondSetGuestScore(20)
                         .thirdSetHostScore(15)
                         .thirdSetGuestScore(10)


    //then
    assert(underTest.isMatchFinished, "Something went wrong!")
  }

  test("Sets second set score, during first set") {

    //given

    //when
    underTest = underTest.firstSetHostScore(21)

    intercept[IllegalArgumentException] {
      underTest.secondSetGuestScore(4)
    }

  }

  test("Sets third set score, during second set") {

    //given

    //when
    underTest.firstSetHostScore(21)
      .firstSetGuestScore(19)
      .secondSetHostScore(21)
      .secondSetGuestScore(20)
    //then

    intercept[IllegalArgumentException] {
      underTest.thirdSetGuestScore(4)
    }
  }

  test("Sets negative score") {
    //given

    //when

    intercept[IllegalArgumentException] {
      underTest.firstSetGuestScore(-2)
    }
  }

  test("Sets set score - advantages") {
    //given

    //when
    underTest = underTest.firstSetGuestScore(32)
      .firstSetHostScore(30)
      .secondSetHostScore(23)
      .secondSetGuestScore(21)
      .thirdSetGuestScore(18)
      .thirdSetHostScore(16)

    //then
    assert(underTest.isMatchFinished, "Something went wrong!")
  }

  test("toJson test") {

    //given
    val rightJson = Json.parse(
      """{ "score" : { "sets" : [
        |{"1" : { "host" : 21 , "guest" : 15 }},
        |{"2": { "host" : 19, "guest" : 21}},
        |{"3": { "host" : 17, "guest" : 15}}]}}""".stripMargin)

    //when
    underTest = underTest.firstSetHostScore(21)
    .firstSetGuestScore(15)
    .secondSetHostScore(19)
    .secondSetGuestScore(21)
    .thirdSetHostScore(17)
    .thirdSetGuestScore(15)
    val underTestJson = underTest.toJson

    //then
    assert(rightJson.equals(underTestJson), "Json generation went wrong")
  }
}

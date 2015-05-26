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

  test("toJson test") {

    //given
    val rightJson = Json.parse(
      """{ "score" : { "sets" : [
        |{"1" : { "host" : 21 , "guest" : 15 }},
        |{"2": { "host" : 19, "guest" : 21}},
        |{"3": { "host" : 17, "guest" : 15}}]}}""".stripMargin)

    //when
    underTest.addSet()
    underTest.setScoreInLastSet(21,15)
    underTest.addSet()
    underTest.setScoreInLastSet(19,21)
    underTest.addSet()
    underTest.setScoreInLastSet(17,15)
    val underTestJson = underTest.toJson

    //then
    assert(underTest.isHostWinner(), "Something went!")
    assert(rightJson.equals(underTestJson), "Json generation went wrong")
  }
}

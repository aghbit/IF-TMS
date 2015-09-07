package models.strategy.strategies

import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.scalatest.FunSuite
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 07.09.15.
 */
class RoundRobinStrategy$Test extends FunSuite {

  test("testInitEmpty") {

    //given
    val underTest = RoundRobinStrategy.initEmpty(new ObjectId(), 10, BeachVolleyball)
    //when

    println(underTest.toString())
    //then
  }

}

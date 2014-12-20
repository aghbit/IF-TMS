package it

import models.strategy.TournamentStrategy
import models.strategy.strategies.SingleEliminationStrategy
import models.team.Team
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner

/**
 * Created by ludwik on 13.12.14.
 */
@RunWith(classOf[JUnitRunner])
class GenerateTreeIT extends FunSuite with BeforeAndAfter with MockitoSugar {
  var underTest: TournamentStrategy = _

  before {

  }
  test("generateTree: Simple integration test 1") {

    //given
    var my_list = List(mock[Team])
    for(i <- 0 until 14) my_list = mock[Team]::my_list
    underTest = new SingleEliminationStrategy(my_list)
    val expectedDeph = 4
    val tree = underTest.generateTree(my_list)

    //when

    var count: Int = 0
    var tmp = tree.root
    while(tmp!=null){
      count = count +1
      tmp = tmp.left
    }

    //then

    assert(count==expectedDeph, "Genereting integrated treee doesnt work.")

  }

  test("generateTree: Simple integration test 2") {

    //given

    var my_list = List(mock[Team])
    for(i <- 0 until 20) my_list = mock[Team]::my_list
    underTest = new SingleEliminationStrategy(my_list)
    val expectedDeph = 5
    val tree = underTest.generateTree(my_list)

    //when

    var count: Int = 0
    var tmp = tree.root
    while(tmp!=null){
      count = count +1
      tmp = tmp.left
    }

    //then

    assert(count==expectedDeph, "Genereting integrated treee doesnt work.")

  }
  test("generateTree: Simple integration test 3") {

    //given

    var my_list = List(mock[Team])
    underTest = new SingleEliminationStrategy(my_list)
    val expectedDeph = 1
    val tree = underTest.generateTree(my_list)

    //when

    var count: Int = 0
    var tmp = tree.root
    while(tmp!=null){
      count = count +1
      tmp = tmp.left
    }

    //then

    assert(count==expectedDeph, "Genereting integrated treee doesnt work.")

  }





}

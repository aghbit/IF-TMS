
import models.strategy.Tree.EliminationTree
import models.strategy.strategies.{DoubleEliminationStrategy, NotEnoughTeamsException}
import models.team.Team
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-14.
 */
@RunWith(classOf[JUnitRunner])
class  DoubleEliminationStrategyTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  var underTest:DoubleEliminationStrategy = _
  var listOfTeams:List[Team] = _
  var tree:EliminationTree = _


  before{
      listOfTeams = List()           // zrobic testy dla generateTree
  }

  test("populateTree: too few teams"){
    //given
    listOfTeams = List(mock[Team],mock[Team],mock[Team])
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
    //when


    //then
    intercept[NotEnoughTeamsException]{
      underTest.populateTree(tree, listOfTeams)
    }
  }

  test("populateTree: 2^n teams"){
    //given
    for(i<-0 until 16) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
    underTest.populateTree(tree,listOfTeams)
    //when
    val testDrawing1:Boolean = underTest.areAllTeamsSet(tree,listOfTeams)
    val testDrawing2:Boolean = underTest.is2ndand4thQuarterEmpty(tree.root)
    //then
    assert(testDrawing1,"populateTree: 2^n teams1")
    assert(testDrawing2,"populateTree: 2^n teams2")
  }

  test("populateTree: 2^n-1 teams"){
    //given
    for(i<-0 until 15) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
    underTest.populateTree(tree,listOfTeams)
    //when
    val testDrawing1:Boolean = underTest.areAllTeamsSet(tree,listOfTeams)
    val testDrawing2:Boolean = underTest.is2ndand4thQuarterEmpty(tree.root)
    //then
    assert(testDrawing1,"populateTree: 2^n-1 teams1")
    assert(testDrawing2,"populateTree: 2^n-1 teams2")
  }

  test("populateTree: 2k teams where 2^(n-1)<2k<2^n"){
    //given
    for(i<-0 until 24) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
    underTest.populateTree(tree,listOfTeams)
    //when
    val testDrawing1:Boolean = underTest.areAllTeamsSet(tree,listOfTeams)
    val testDrawing2:Boolean = underTest.is2ndand4thQuarterEmpty(tree.root)
    //then
    assert(testDrawing1,"populateTree: 2k teams where 2^(n-1)<2k<2^n 1")
    assert(testDrawing2,"populateTree: 2k teams where 2^(n-1)<2k<2^n 2")
  }

}

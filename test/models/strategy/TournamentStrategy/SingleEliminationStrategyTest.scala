

import models.strategy.TournamentStrategy
import models.strategy.Tree.EliminationTree
import models.strategy.strategies.SingleEliminationStrategy
import models.team.Team
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import reactivemongo.bson.BSONObjectID

/**
 * Created by ludwik on 13.12.14.
 */
@RunWith(classOf[JUnitRunner])
class SingleEliminationStrategyTest extends FunSuite with BeforeAndAfter with MockitoSugar {
    var underTest: TournamentStrategy = _

  before{

  }

  test("generateTree: Simple test 2"){

    //given

     var A = List(mock[Team])
    for(i <- 0 until 14)A =mock[Team]::A //have 15 elements in List of Teams
     underTest = new SingleEliminationStrategy(A)

    //when

    var tmp = underTest.generateTree(A).root
    var count: Int = 0
    while(tmp!=null){
      tmp = tmp.left
      count+=1
    }
    val condition = count==4 //deph of tree should be 4 levels

    //then

    assert(condition, "Generate Tree Simple Test doesnt work.")
  }
  test("generateTree: Simple test 1"){

    //given

    underTest = new SingleEliminationStrategy(List(mock[Team]))

    //when

    val tree = underTest.generateTree(List(mock[Team]))
    val condition = tree match  {
      case t:EliminationTree => true
      case _ => false
    }

    //then

    assert(condition, "Generate Tree Simple Test doesnt work.")
  }
  test("PopulateTree: Simple test"){

    //given

    var A = List(mock[Team])
    for(i <- 0 until 7) A=mock[Team]::A //Added 7+1 teams to my list
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.left
    }
    val condition = !(tmp.value==null)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 2"){

    //given

    var A = List(mock[Team])
    for(i <- 0 until 17) A=mock[Team]::A //Added 17 teams to my list
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.right
    }
    val condition = !(tmp.value==null)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 3"){

    //given

    var A = List(mock[Team])
    for(i <- 0 until 19) A=mock[Team]::A //Added 20 teams to my list
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.left
    }
    tmp = tmp.parent
    val condition = (tmp.value==null)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 4"){

    //given

    var A = List(mock[Team])
    for(i <- 0 until 18) A=mock[Team]::A //Added 7+1 teams to my list
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.left
    }
    tmp = tmp.parent.right
    val condition = !(tmp.value==null)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 5"){

    //given


    var A = List(mock[Team])
    for(i <- 0 until 18){ //Adding 18 teams
      val B = mock[Team]
      Mockito.when(B._id).thenReturn(BSONObjectID.generate) //Generate Team id
      A = B::A
    }
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.left
    }

    val condition = (tmp.value.host!=null &&tmp.value.guest!=null)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }

  test("PopulateTree: Simple test 6"){

    //given


    var A = List(mock[Team])
    for(i <- 0 until 18){
      var B = mock[Team]
      Mockito.when(B._id).thenReturn(BSONObjectID.generate)
      A = B::A
    }
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.right
    }

    val condition = (tmp.value.host!=null &&tmp.value.guest==null)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 7"){

    //given


    var A = List(mock[Team])
    for(i <- 0 until 18){
      var B = mock[Team]
      Mockito.when(B._id).thenReturn(BSONObjectID.generate)
      A = B::A
    } //Added 18 teams to my list
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.left
    }

    val condition = (tmp.parent.right.value.guest!=null && tmp.parent.right.value.host!=null)
    val cond2 = (tmp.parent.parent.right.left.value.guest==null && tmp.parent.parent.right.left.value.host!=null)

    //then

    assert(((condition) && (cond2)), "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 8"){

    //given


    var A = List(mock[Team])
    for(i <- 0 until 18){
      var B = mock[Team]
      Mockito.when(B._id).thenReturn(BSONObjectID.generate)
      A = B::A
    } //Added 18 teams to my list
    underTest = new SingleEliminationStrategy(A)
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.populateTree(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=null){
      count+=1 //counting deph
      tmp=tmp.left
    }
    //I have 19 teams, so I should have 3 full matches

    //checking first match
    val cond1 = tmp.value.guest!=null && tmp.value.host!=null
    //checking second match; should be full (guest and host filled)
    val condition = (tmp.parent.right.value.guest!=null && tmp.parent.right.value.host!=null)
   //checking third match, should have only guest
    val cond2 = (tmp.parent.parent.right.left.value.guest==null && tmp.parent.parent.right.left.value.host!=null)

    //then

    assert(((condition) && (cond2) && cond1), "Populating SingleElminationStrategy Test doesnt work.")
  }
}

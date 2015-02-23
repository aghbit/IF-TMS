

import models.Game.EliminationTree
import models.strategy.{TournamentStrategy}
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
     underTest = new SingleEliminationStrategy()

    //when

    var tmp = underTest.generateTree(A).root
    var count: Int = 1
    while(tmp.left!=None){
      tmp = tmp.left.get
      count+=1
    }
    val condition = count==4 //deph of tree should be 4 levels

    //then

    assert(condition, "Generate Tree Simple Test doesnt work.")
  }
  test("generateTree: Simple test 1"){

    //given

    underTest = new SingleEliminationStrategy()

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
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.left.get
    }
    val condition = !(tmp.value==None)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 2"){

    //given

    var A = List(mock[Team])
    for(i <- 0 until 17) A=mock[Team]::A //Added 17 teams to my list
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.right.get
    }
    val condition = !(tmp.value==None)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 3"){

    //given

    var A = List(mock[Team])
    for(i <- 0 until 19) A=mock[Team]::A //Added 20 teams to my list
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.left.get
    }
    tmp = tmp.parent.get
    val condition = (tmp.value==None)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 4"){

    //given

    var A = List(mock[Team])
    for(i <- 0 until 18) A=mock[Team]::A //Added 7+1 teams to my list
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.left.get
    }
    tmp = tmp.parent.get.right.get
    val condition = !(tmp.value==None)

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
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.left.get
    }

    val condition = (tmp.value.get.host!=None &&tmp.value.get.guest!=None)

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
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.right.get
    }

    val condition = (tmp.value.get.host!=None &&tmp.value.get.guest==None)

    //then

    assert(condition, "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 7"){

    //given


    var A = List(mock[Team])
    for(i <- 0 until 17){
      var B = mock[Team]
      Mockito.when(B._id).thenReturn(BSONObjectID.generate)
      A = B::A
    } //Added 18 teams to my list
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.left.get
    }

    val condition = (tmp.parent.get.right.get.value.get.guest!=None && tmp.parent.get.right.get.value.get.host!=None)
    val cond2 = ( tmp.parent.get.parent.get.right.get.left.get.value.get.host!=None)
    val cond3 = tmp.parent.get.parent.get.right.get.left.get.value.get.guest==None
    //then

    assert(((condition) && (cond2) && cond3), "Populating SingleElminationStrategy Test doesnt work.")
  }
  test("PopulateTree: Simple test 8"){

    //given


    var A = List(mock[Team])
    for(i <- 0 until 17){
      var B = mock[Team]
      Mockito.when(B._id).thenReturn(BSONObjectID.generate)
      A = B::A
    } //Added 18 teams to my list
    underTest = new SingleEliminationStrategy()
    var tree = underTest.generateTree(A) //Genereting EMPTY tree (nulls inside)


    //when

    val x = underTest.drawTeamsInTournament(tree,A)
    var tmp = x.root
    var count: Int = 0
    while(tmp.left!=None){
      count+=1 //counting deph
      tmp=tmp.left.get
    }
    //I have 19 teams, so I should have 3 full matches

    //checking first match
    val cond1 = tmp.value.get.guest!=None && tmp.value.get.host!=None
    //checking second match; should be full (guest and host filled)
    val condition = (tmp.parent.get.right.get.value.get.guest!=None && tmp.parent.get.right.get.value.get.host!=None)
   //checking third match, should have only guest
    val cond2 = (tmp.parent.get.parent.get.right.get.left.get.value.get.guest==None && tmp.parent.get.parent.get.right.get.left.get.value.get.host!=None)

    //then

    assert(((condition) && (cond2) && cond1), "Populating SingleElminationStrategy Test doesnt work.")
  }
}

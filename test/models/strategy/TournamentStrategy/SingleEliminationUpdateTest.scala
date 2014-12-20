package models.strategy.strategies

import models.strategy.{VSet, TournamentStrategy}
import models.strategy.Tree.EliminationTree
import models.strategy.scores.VolleyballScore
import models.strategy.strategies.{DoubleEliminationStrategy, SingleEliminationStrategy}
import models.team.Team
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import reactivemongo.bson.BSONObjectID

import scala.util.Random


/**
 * Created by ludwik on 17.12.14.
 */
@RunWith(classOf[JUnitRunner])
class SingleEliminationUpdateTest extends FunSuite with BeforeAndAfter with MockitoSugar {
  var underTest:SingleEliminationStrategy = _
  var listOfTeams:List[Team] = _
  var tree:EliminationTree = _

  before{

  }


  test("UpdateTree: playing 8-team tournament, not playing all matches"){
    //given

    listOfTeams = List()
    for(i<-0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    //score Part
    val score:VolleyballScore = mock[VolleyballScore]
    val score2:VolleyballScore = mock[VolleyballScore]
    Mockito.when(score.isEnded).thenReturn(true)


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"ll")
    val game2 = underTest.getGame(tree.root,"lr")
    val game3 = underTest.getGame(tree.root,"rl")
    val game4 = underTest.getGame(tree.root,"rr")
    val games = List(game1,game2,game3,game4)

    games.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")



    val newGame3 = updatedTree.root;

    //////////////////////////////////////

    //then
    assert(newGame3.value===null,"UpdateTree: playing 8-team tournament without winner")
    assert(game5.value!=null,"UpdateTree: playing 8-team tournament without winner")
    assert(game6.value!=null,"UpdateTree: playing 8-team tournament without winner")

  }
  test("UpdateTree: playing 8-team tournament, checking one update"){
    //given
    listOfTeams = List()

    for(i<-0 until 8) {
      val team = mock[Team]
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team "+i)
      listOfTeams = team :: listOfTeams
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    //score Part


    val VSetw = mock[VSet]
    val VSetl = mock[VSet]
    Mockito.when(VSetw.won).thenReturn(true)
    Mockito.when(VSetl.won).thenReturn(false)

    val setsW = List(VSetw,VSetw,VSetw)
    val setsL = List(VSetl,VSetl,VSetl)


    val score:VolleyballScore = VolleyballScore()
    val score2:VolleyballScore = VolleyballScore()

    score.sets=setsW
    score2.sets=setsL

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"ll")
    val game2 = underTest.getGame(tree.root,"lr")
    val game3 = underTest.getGame(tree.root,"rl")
    val game4 = underTest.getGame(tree.root,"rr")
    val games = List(game1,game2,game3,game4)
    games.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))



    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    //Now checking correct update
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")



    val condition0 = game5.value.host!=null && game5.value.guest!=null && game1.value.host!=null//  ERROR HERE
    val condition1 = game5.value.host.toString().equals(game1.value.host.toString())//  ERROR HERE
    val condition2 = game5.value.guest.toString().equals(game2.value.host.toString())

    System.out.println("Game 5 have host"+game5.value.host.toString())
    System.out.println("Game 5 have guest"+game5.value.guest.toString())

    assert(game1.value.host!=null,"UpdateTree: playing 8-team tournament, testing one check")
    assert(tree.root.left.value!=null,"UpdateTree: playing 8-team tournament, testing one check")
    assert(tree.root.value==null,"UpdateTree: playing 8-team tournament, testing one check")
    assert(condition0,"UpdateTree: playing 8-team tournament, testing one check")
    assert(condition1,"UpdateTree: playing 8-team tournament, testing one check")
    assert(condition2,"UpdateTree: playing 8-team tournament, testing one check")
  }
  test("UpdateTree: playing 8-team tournament, checking the winner"){
    //given
    listOfTeams = List()

    for(i<-0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team "+i)
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    //score Part


    val VSetw = mock[VSet]
    val VSetl = mock[VSet]
    Mockito.when(VSetw.won).thenReturn(true)
    Mockito.when(VSetl.won).thenReturn(false)

    val setsW = List(VSetw,VSetw,VSetw)
    val setsL = List(VSetl,VSetl,VSetl)


    val score:VolleyballScore = VolleyballScore()
    val score2:VolleyballScore = VolleyballScore()

    score.sets=setsW
    score2.sets=setsL


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"ll")
    val game2 = underTest.getGame(tree.root,"lr")
    val game3 = underTest.getGame(tree.root,"rl")
    val game4 = underTest.getGame(tree.root,"rr")
    val games = List(game1,game2,game3,game4)

    games.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    val winnerList = games.map(game=>game.value.winningTeam)
    val loserList = games.map(game=>game.value.losingTeam)
    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")

    game5.value.scoreHost = score
    game5.value.scoreGuest = score2

    game6.value.scoreHost = score
    game6.value.scoreGuest = score2

    val finalmatch = underTest.updateTree(tree).root

    //then
   assert(finalmatch!=null,"Update Tree: FinalMatch is null")
   val condition = (finalmatch.value.host==game1.value.host)//checking first team in final
   val condition2 = finalmatch.value.guest==game3.value.host //checking the second team

    assert(condition, "Update Tree: 8 teams match, checking the final")
    assert(condition2,"Update Tree: 8 teams match, checking the final")
  }
  test("UpdateTree: playing 8-team tournament, checking the winner 2"){

    //given

    listOfTeams = List()
    for(i<-0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team "+i)
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    //score Part


    val VSetw = mock[VSet]
    val VSetl = mock[VSet]
    Mockito.when(VSetw.won).thenReturn(true)
    Mockito.when(VSetl.won).thenReturn(false)

    val setsW = List(VSetw,VSetw,VSetw)
    val setsL = List(VSetl,VSetl,VSetl)


    val score:VolleyballScore = VolleyballScore()
    val score2:VolleyballScore = VolleyballScore()

    score.sets=setsW
    score2.sets=setsL

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"ll")
    val game2 = underTest.getGame(tree.root,"lr")
    val game3 = underTest.getGame(tree.root,"rl")
    val game4 = underTest.getGame(tree.root,"rr")
    val games = List(game1,game2,game3,game4)

    game1.value.scoreHost = score2
    game1.value.scoreGuest = score // wins second

    game2.value.scoreHost = score
    game2.value.scoreGuest = score2 // wins third

    game3.value.scoreHost = score
    game3.value.scoreGuest = score2//wins fifth

    game4.value.scoreHost = score2
    game4.value.scoreGuest = score //wins 8.

    ///////////////////////////////////////
    underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")

    game5.value.scoreHost = score2
    game5.value.scoreGuest = score //wins 3.

    game6.value.scoreHost = score2
    game6.value.scoreGuest = score //wins 8.

    underTest.updateTree(tree)

    val finalmatch = underTest.updateTree(tree).root

    //then

    val condition = finalmatch.value.host==game2.value.host//checking first team in final
    val condition2 = finalmatch.value.guest==game4.value.guest //checking the second team

    assert(condition, "Update Tree: 8 teams match, checking the final")
    assert(condition2,"Update Tree: 8 teams match, checking the final")
  }
  test("UpdateTree: playing 8-team tournament, whole matches simulation"){

    //given

    listOfTeams = List()
    for(i<-0 until 16) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team "+i)
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    //score Part

    /// Scores 3:0
    val VSetw = mock[VSet]
    val VSetl = mock[VSet]

    Mockito.when(VSetw.won).thenReturn(true)
    Mockito.when(VSetl.won).thenReturn(false)

    val setsW = List(VSetw,VSetw,VSetw)
    val setsL = List(VSetl,VSetl,VSetl)


    val score:VolleyballScore = VolleyballScore()
    val score2:VolleyballScore = VolleyballScore()

    score.sets=setsW
    score2.sets=setsL

    /// Scores 3:2

    val setsW2 = List(VSetw,VSetw,VSetw)
    val setsL2 = List(VSetw,VSetw,VSetl)


    val score21:VolleyballScore = VolleyballScore()
    val score22:VolleyballScore = VolleyballScore()

    score21.sets=setsW2
    score22.sets=setsL2

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"lll")
    val game2 = underTest.getGame(tree.root,"llr")
    val game3 = underTest.getGame(tree.root,"lrl")
    val game4 = underTest.getGame(tree.root,"lrr")

    val game5 = underTest.getGame(tree.root,"rll")
    val game6 = underTest.getGame(tree.root,"rlr")
    val game7 = underTest.getGame(tree.root,"rrl")
    val game8 = underTest.getGame(tree.root,"rrr")

    val games = List(game1,game2,game3,game4,game5,game6,game7,game8)


    games.foreach(game =>(game.value.scoreHost = score,game.value.scoreGuest = score2))
    var updatedTree = underTest.updateTree(tree) // updating matches
    //Right now play : 1 vs 3, 5 vs 7, 9 vs 11, 13 vs 15
    val game9 = underTest.getGame(tree.root,"ll")
    val game10 = underTest.getGame(tree.root,"lr")
    val game11 = underTest.getGame(tree.root,"rl")
    val game12 = underTest.getGame(tree.root,"rr")


    game9.value.scoreHost = score2
    game9.value.scoreGuest = score

    game10.value.scoreHost = score21
    game10.value.scoreGuest = score22

    game11.value.scoreHost = score2
    game11.value.scoreGuest = score

    game12.value.scoreHost = score2
    game12.value.scoreGuest = score
    updatedTree=underTest.updateTree(tree)
    //right now play: 3 vs 5 and 11 vs 15

    val game13 = underTest.getGame(tree.root,"l")
    val game14 = underTest.getGame(tree.root,"r")

    game13.value.scoreHost = score22
    game13.value.scoreGuest = score21

    game14.value.scoreHost = score2
    game14.value.scoreGuest = score

    updatedTree=underTest.updateTree(tree)
    //in final should be 3 and 15 team
    val condition0 = (tree.root.left.value.host==game2.value.host && tree.root.left.value.guest==game3.value.host)
    val condition1 = (tree.root.value.host==game3.value.host && tree.root.value.guest == game8.value.host)




    assert(condition0, "Update Tree: 16 teams match, checking semi final match")
    assert(condition1, "Update Tree: 16 teams match, checking final teams")
  }
  test("UpdateTree: playing 8-team tournament, 10 teams whole 1. phase"){

    //given

    listOfTeams = List()
    for(i<-0 until 10) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team "+i)
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    //score Part

    /// Scores 3:1
    val VSetw = mock[VSet]
    val VSetl = mock[VSet]

    Mockito.when(VSetw.won).thenReturn(true)
    Mockito.when(VSetl.won).thenReturn(false)

    val setsW = List(VSetl,VSetw,VSetw)
    val setsL = List(VSetw,VSetl,VSetl)


    val score:VolleyballScore = VolleyballScore()
    val score2:VolleyballScore = VolleyballScore()

    score.sets=setsW
    score2.sets=setsL

    /// Scores 3:2

    val setsW2 = List(VSetw,VSetw,VSetw)
    val setsL2 = List(VSetw,VSetw,VSetl)


    val score21:VolleyballScore = VolleyballScore()
    val score22:VolleyballScore = VolleyballScore()

    score21.sets=setsW2
    score22.sets=setsL2

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"lll")
    val game2 = underTest.getGame(tree.root,"llr")
    val game3 = underTest.getGame(tree.root,"lrl")
    val game4 = underTest.getGame(tree.root,"lrr")

    val game5 = underTest.getGame(tree.root,"rll")
    val game6 = underTest.getGame(tree.root,"rlr")
    val game7 = underTest.getGame(tree.root,"rrl")
    val game8 = underTest.getGame(tree.root,"rrr")

    val games = List(game1,game2,game3,game4,game5,game6,game7,game8)

    def setWinLooseSet = {if(new Random().nextInt(10)>5) // Returns set 3:1 or 3:2
      List(score,score2)
      else
      List(score21,score22)
    }
    games.foreach(game =>(game.value.scoreHost = setWinLooseSet(0),game.value.scoreGuest = setWinLooseSet(1)))

    underTest.updateTree(tree) // updating matches
    //Right now play : 1 vs 3, 5 vs 7, 9 vs 11, 13 vs 15
    val game9 = underTest.getGame(tree.root,"ll")
    val game10 = underTest.getGame(tree.root,"lr")
    val game11 = underTest.getGame(tree.root,"rl")
    val game12 = underTest.getGame(tree.root,"rr")

    var updatedTree = underTest.updateTree(tree)
    //checking third math
    val condition0 = (game11.value.host==game5.value.host && game11.value.guest ==game6.value.host)
    val condition1 = (game4.value.host!=null && game4.value.guest==null)

    assert(condition0, "Update Tree: 10 teams match, checking third match after one update")
    assert(condition1, "Update Tree: 10 teams match, checking 4. match ")
  }

}

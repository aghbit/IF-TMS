
import models.Game.EliminationTree
import models.strategy.{VSet, TournamentStrategy}
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

/*
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
    tree = underTest.drawTeamsInTournament(tree,listOfTeams)

    //when
    //score Part
    val score:VolleyballScore = mock[VolleyballScore]
    val score2:VolleyballScore = mock[VolleyballScore]
    Mockito.when(score.isMatchFinished).thenReturn(true)


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"ll")
    val game2 = underTest.getGame(tree.root,"lr")
    val game3 = underTest.getGame(tree.root,"rl")
    val game4 = underTest.getGame(tree.root,"rr")
    val games = List(game1,game2,game3,game4)

    games.foreach(game => (game.value.get.scoreHost=score, game.value.get.scoreGuest=score2))

    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")



    val newGame3 = updatedTree.root

    //////////////////////////////////////

    //then
    assert(newGame3.value===None,"UpdateTree: playing 8-team tournament without winner")
    assert(game5.value!=None,"UpdateTree: playing 8-team tournament without winner")
    assert(game6.value!=None,"UpdateTree: playing 8-team tournament without winner")

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
    tree = underTest.drawTeamsInTournament(tree,listOfTeams)

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
    games.foreach(game => (game.value.get.scoreHost=score, game.value.get.scoreGuest=score2))



    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    //Now checking correct update
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")



    val condition0 = game5.value.get.host!=None && game5.value.get.guest!=None && game1.value.get.host!=None
    val condition1 = game5.value.get.host.toString().equals(game1.value.get.host.toString())
    val condition2 = game5.value.get.guest.toString().equals(game2.value.get.host.toString())



    assert(game1.value.get.host!=None,"UpdateTree: playing 8-team tournament, testing one check")
    assert(tree.root.left.get.value!=None,"UpdateTree: playing 8-team tournament, testing one check")
    assert(tree.root.value==None,"UpdateTree: playing 8-team tournament, testing one check")
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
    tree = underTest.drawTeamsInTournament(tree,listOfTeams)

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

    games.foreach(game => (game.value.get.scoreHost=score, game.value.get.scoreGuest=score2))

    val winnerList = games.map(game=>game.value.get.winningTeam)
    val loserList = games.map(game=>game.value.get.losingTeam)
    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")

    game5.value.get.scoreHost = score
    game5.value.get.scoreGuest = score2

    game6.value.get.scoreHost = score
    game6.value.get.scoreGuest = score2

    val finalmatch = underTest.updateTree(tree).root

    //then
   assert(finalmatch!=None,"Update Tree: FinalMatch is null")
   val condition = finalmatch.value.get.host==game1.value.get.host//checking first team in final
   val condition2 = finalmatch.value.get.guest==game3.value.get.host //checking the second team

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
    tree = underTest.drawTeamsInTournament(tree,listOfTeams)

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

    game1.value.get.scoreHost = score2
    game1.value.get.scoreGuest = score // wins second

    game2.value.get.scoreHost = score
    game2.value.get.scoreGuest = score2 // wins third

    game3.value.get.scoreHost = score
    game3.value.get.scoreGuest = score2//wins fifth

    game4.value.get.scoreHost = score2
    game4.value.get.scoreGuest = score //wins 8.

    ///////////////////////////////////////
    underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root,"l")
    val game6 = underTest.getGame(tree.root,"r")

    game5.value.get.scoreHost = score2
    game5.value.get.scoreGuest = score //wins 3.

    game6.value.get.scoreHost = score2
    game6.value.get.scoreGuest = score //wins 8.

    underTest.updateTree(tree)

    val finalmatch = underTest.updateTree(tree).root

    //then

    val condition = finalmatch.value.get.host==game2.value.get.host//checking first team in final
    val condition2 = finalmatch.value.get.guest==game4.value.get.guest //checking the second team

    assert(condition, "Update Tree: 8 teams match, checking the final")
    assert(condition2,"Update Tree: 8 teams match, checking the final")
  }
  test("UpdateTree: playing 12-team tournament, 1 phase"){

    //given

    listOfTeams = List()
    for(i<-0 until 12) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team "+i)
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree,listOfTeams)

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


    games.foreach(game =>(game.value.get.scoreHost = score2,game.value.get.scoreGuest = score))
    var updatedTree = underTest.updateTree(tree) // updating matches

    val game9 = underTest.getGame(tree.root,"ll")
    val game10 = underTest.getGame(tree.root,"lr")
    val game11 = underTest.getGame(tree.root,"rl")
    val game12 = underTest.getGame(tree.root,"rr")

  val condition1 = (game9.value.get.host==game1.value.get.guest && game9.value.get.guest == game2.value.get.guest)
  val condition2 = (game10.value.get.host==game3.value.get.guest && game10.value.get.guest == game4.value.get.guest)
  val condition3 = (game11.value.get.host==game5.value.get.host && game11.value.get.guest==game6.value.get.host)


    assert(condition1, "Update Tree: 12 teams, update after 1 phase")
    assert(condition2, "Update Tree: 12 teams, update after 1 phase")
    assert(condition3, "Update Tree: 12 teams, update after 1 phase")
  }
  test("UpdateTree: playing 12-team tournament,update to final"){

    //given

    listOfTeams = List()
    for(i<-0 until 12) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team "+i)
    }
    underTest=SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree,listOfTeams)

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


    games.foreach(game =>(game.value.get.scoreHost = score2,game.value.get.scoreGuest = score))
    var updatedTree = underTest.updateTree(tree) // updating matches

    val game9 = underTest.getGame(tree.root,"ll")
    val game10 = underTest.getGame(tree.root,"lr")
    val game11 = underTest.getGame(tree.root,"rl")
    val game12 = underTest.getGame(tree.root,"rr")

    game9.value.get.scoreHost = score
    game9.value.get.scoreGuest = score2

    game10.value.get.scoreHost = score2
    game10.value.get.scoreGuest = score

    game11.value.get.scoreHost = score
    game11.value.get.scoreGuest = score2

    game12.value.get.scoreHost = score2
    game12.value.get.scoreGuest = score// 9 vs 12 and 5 vs 8

    updatedTree = underTest.updateTree(tree)

    val game13= underTest.getGame(tree.root,"l")
    val game14 = underTest.getGame(tree.root,"r")

    game13.value.get.scoreHost = score2
    game13.value.get.scoreGuest = score

    game14.value.get.scoreHost = score2
    game14.value.get.scoreGuest = score// final is 12 vs 8

    var updatedTree2 = underTest.updateTree(tree)

    val condition = (tree.root.value.get.host==game4.value.get.guest)
    val condition2 = (tree.root.value.get.guest==game8.value.get.host)
    assert(condition, "Update Tree: 12 teams, update to final")
    assert(condition2, "Update Tree: 12 teams, update to final")

  }
  test("UpdateTree: playing 16-team tournament, whole match simulation"){

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
    tree = underTest.drawTeamsInTournament(tree,listOfTeams)

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


    games.foreach(game =>(game.value.get.scoreHost = score,game.value.get.scoreGuest = score2))
    var updatedTree = underTest.updateTree(tree) // updating matches
    //Right now play : 1 vs 3, 5 vs 7, 9 vs 11, 13 vs 15
    val game9 = underTest.getGame(tree.root,"ll")
    val game10 = underTest.getGame(tree.root,"lr")
    val game11 = underTest.getGame(tree.root,"rl")
    val game12 = underTest.getGame(tree.root,"rr")


    game9.value.get.scoreHost = score2
    game9.value.get.scoreGuest = score

    game10.value.get.scoreHost = score21
    game10.value.get.scoreGuest = score22

    game11.value.get.scoreHost = score2
    game11.value.get.scoreGuest = score

    game12.value.get.scoreHost = score2
    game12.value.get.scoreGuest = score
    updatedTree=underTest.updateTree(tree)
    //right now play: 3 vs 5 and 11 vs 15

    val game13 = underTest.getGame(tree.root,"l")
    val game14 = underTest.getGame(tree.root,"r")

    game13.value.get.scoreHost = score22
    game13.value.get.scoreGuest = score21

    game14.value.get.scoreHost = score2
    game14.value.get.scoreGuest = score

    updatedTree=underTest.updateTree(tree)
    //in final should be 3 and 15 team
    val condition0 = (tree.root.left.get.value.get.host==game2.value.get.host && tree.root.left.get.value.get.guest==game3.value.get.host)
    val condition1 = (tree.root.value.get.host==game3.value.get.host && tree.root.value.get.guest == game8.value.get.host)




    assert(condition0, "Update Tree: 16 teams match, checking semi final match")
    assert(condition1, "Update Tree: 16 teams match, checking final teams")
  }*/
}


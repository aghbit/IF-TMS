

import models.Game.EliminationTree
import models.exceptions.NotEnoughTeamsException
import models.strategy.VSet
import models.strategy.scores.VolleyballScore
import models.strategy.strategies.DoubleEliminationStrategy
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
class DoubleEliminationStrategyTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  var underTest: DoubleEliminationStrategy = _
  var listOfTeams: List[Team] = _
  var tree: EliminationTree = _


  before {
    listOfTeams = List()
  }


  test("populateTree: too few teams") {
    //given
    listOfTeams = List(mock[Team], mock[Team], mock[Team])
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    //when


    //then
    intercept[NotEnoughTeamsException] {
      underTest.drawTeamsInTournament(tree, listOfTeams)
    }
  }

  test("populateTree: 2^n teams") {
    //given
    for (i <- 0 until 16) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)
    //when
    val testDrawing1: Boolean = underTest.areAllTeamsSet(tree, listOfTeams)
    val testDrawing2: Boolean = underTest.is2ndAnd4thQuarterEmpty(tree.root)
    //then
    assert(testDrawing1, "populateTree: 2^n teams1")
    assert(testDrawing2, "populateTree: 2^n teams2")
    assert(underTest.getGame(tree.root, "").value == None, "populateTree: 2^n teams3")
    assert(underTest.getGame(tree.root, "lll").value == None, "populateTree: 2^n teams4")
    assert(underTest.getGame(tree.root, "llll").value != None, "populateTree: 2^n teams5")
  }

  test("populateTree: 2^n-1 teams") {
    //given
    for (i <- 0 until 15) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    underTest.drawTeamsInTournament(tree, listOfTeams)
    //when
    val testDrawing1: Boolean = underTest.areAllTeamsSet(tree, listOfTeams)
    val testDrawing2: Boolean = underTest.is2ndAnd4thQuarterEmpty(tree.root)
    //then
    assert(testDrawing1, "populateTree: 2^n-1 teams1")
    assert(testDrawing2, "populateTree: 2^n-1 teams2")
  }

  test("populateTree: 2k teams where 2^(n-1)<2k<2^n") {
    //given
    for (i <- 0 until 24) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    underTest.drawTeamsInTournament(tree, listOfTeams)
    //when
    val testDrawing1: Boolean = underTest.areAllTeamsSet(tree, listOfTeams)
    val testDrawing2: Boolean = underTest.is2ndAnd4thQuarterEmpty(tree.root)
    //then
    assert(testDrawing1, "populateTree: 2k teams where 2^(n-1)<2k<2^n 1")
    assert(testDrawing2, "populateTree: 2k teams where 2^(n-1)<2k<2^n 2")
  }


  test("UpdateTree: Updating first-match in 8-team tournament") {
    //given
    for (i <- 0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)


    //when
    val game1 = underTest.getGame(tree.root, "lll")
    val game2 = underTest.getGame(tree.root, "llr")
    val games = List(game1, game2)
    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    val winner1 = game1.value.get.winningTeam
    val winner2 = game2.value.get.winningTeam
    val loser1 = game1.value.get.losingTeam
    val loser2 = game2.value.get.losingTeam


    val updatedTree = underTest.updateTree(tree)


    val newGame = underTest.getGame(updatedTree.root, "ll")
    val newGameLosers = underTest.getGame(updatedTree.root, "rrr")
    //then
    assert(newGame.value.get.host === winner1, "UpdateTree: Updating first-match in 8-team tournament1")
    assert(newGame.value.get.guest === winner2, "UpdateTree: Updating first-match in 8-team tournament2")
    assert(newGameLosers.value.get.host === loser1, "UpdateTree: Updating first-match in 8-team tournament3")
    assert(newGameLosers.value.get.guest === loser2, "UpdateTree: Updating first-match in 8-team tournament4")
  }
  //
  test("UpdateTree: playing 8-team tournament, not playing all matches") {
    //given
    for (i <- 0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lll")
    val game2 = underTest.getGame(tree.root, "llr")
    val game3 = underTest.getGame(tree.root, "rll")
    val game4 = underTest.getGame(tree.root, "rlr")
    val games = List(game1, game2, game3, game4)

    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root, "ll")
    val game6 = underTest.getGame(tree.root, "rrr")
    val games2 = List(game5, game6)

    games2.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )


    updatedTree = underTest.updateTree(updatedTree)
    val newGame1 = underTest.getGame(updatedTree.root, "l")
    val newGame2 = underTest.getGame(updatedTree.root, "r")
    val newGame3 = underTest.getGame(updatedTree.root, "rr")
    val newGame4 = underTest.getGame(updatedTree.root, "lr")
    //////////////////////////////////////

    //then
    assert(newGame1.value === None, "UpdateTree: playing 8-team tournament, not playing all matches1")
    assert(newGame2.value === None, "UpdateTree: playing 8-team tournament, not playing all matches2")
    assert(newGame3.value === None, "UpdateTree: playing 8-team tournament, not playing all matches3")
    assert(newGame4.value === None, "UpdateTree: playing 8-team tournament, not playing all matches4")
  }
  //
  test("UpdateTree: playing 8-team tournament till the final") {
    //given
    for (i <- 0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lll")
    val game2 = underTest.getGame(tree.root, "llr")
    val game3 = underTest.getGame(tree.root, "rll")
    val game4 = underTest.getGame(tree.root, "rlr")
    val games = List(game1, game2, game3, game4)

    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )



    val winnerList = games.map(game => game.value.get.winningTeam)
    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(updatedTree.root, "ll")
    val game6 = underTest.getGame(updatedTree.root, "lrr")
    val game7 = underTest.getGame(updatedTree.root, "rl")
    val game8 = underTest.getGame(updatedTree.root, "rrr")
    val games2 = List(game5, game6, game7, game8)

    games2.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////3rd Phase///////////////////
    val game9 = underTest.getGame(updatedTree.root, "lr")
    val game10 = underTest.getGame(updatedTree.root, "rr")
    val games3 = List(game9, game10)


    games3.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    /////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////Semi-finals//////////////////
    val game11 = underTest.getGame(updatedTree.root, "l")
    val game12 = underTest.getGame(updatedTree.root, "r")
    val games4 = List(game11, game12)

    games4.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ///////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////Final////////////////////////////
    val game13 = underTest.getGame(updatedTree.root, "")
    val games5 = List(game13)

    games5.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)

    val Final = underTest.getGame(updatedTree.root, "")

    //////////////////////////////////////

    //then
    assert(Final.value.get.winningTeam === winnerList(0), "UpdateTree: playing 8-team tournament till the final1")
    assert(Final.value.get.losingTeam === winnerList(2), "UpdateTree: playing 8-team tournament till the final2")
  }
  //

  test("UpdateTree: playing 5-team tournament till the final Semis Phase") {
    //given
    for (i <- 0 until 5) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }


    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lll")
    val game2 = underTest.getGame(tree.root, "llr")
    val game3 = underTest.getGame(tree.root, "rll")
    val game4 = underTest.getGame(tree.root, "rlr")
    val games = List(game1, game2, game3, game4)


    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )


    val winnerList = games.map(game => game.value.get.winningTeam)

    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(updatedTree.root, "ll")
    val game7 = underTest.getGame(updatedTree.root, "rl")
    val games2 = List(game5, game7)


    games2.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////3rd Phase///////////////////
    val game9 = underTest.getGame(updatedTree.root, "lr")
    val game10 = underTest.getGame(updatedTree.root, "rr")
    val games3 = List(game9, game10)

    games3.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    /////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////Semi-finals//////////////////
    val game11 = underTest.getGame(updatedTree.root, "l")
    val game12 = underTest.getGame(updatedTree.root, "r")
    val games4 = List(game11, game12)

    games4.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ///////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////Final////////////////////////////
    val game13 = underTest.getGame(updatedTree.root, "")
    val games5 = List(game13)

    games5.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)

    val Final = underTest.getGame(updatedTree.root, "")

    //////////////////////////////////////

    //then
    assert(Final.value.get.winningTeam === winnerList(0), "UpdateTree: playing 5-team tournament till the final1")
    assert(Final.value.get.losingTeam === winnerList(2), "UpdateTree: playing 5-team tournament till the final2")
  }

  test("UpdateTree: playing 32-team tournament till the final Final Phase") {
    //given
    for (i <- 0 until 32) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lllll")
    val game2 = underTest.getGame(tree.root, "llllr")
    val game3 = underTest.getGame(tree.root, "lllrl")
    val game4 = underTest.getGame(tree.root, "lllrr")
    val game5 = underTest.getGame(tree.root, "llrll")
    val game6 = underTest.getGame(tree.root, "llrlr")
    val game7 = underTest.getGame(tree.root, "llrrl")
    val game8 = underTest.getGame(tree.root, "llrrr")
    val game9 = underTest.getGame(tree.root, "rllll")
    val game10 = underTest.getGame(tree.root, "rlllr")
    val game11 = underTest.getGame(tree.root, "rllrl")
    val game12 = underTest.getGame(tree.root, "rllrr")
    val game13 = underTest.getGame(tree.root, "rlrll")
    val game14 = underTest.getGame(tree.root, "rlrlr")
    val game15 = underTest.getGame(tree.root, "rlrrl")
    val game16 = underTest.getGame(tree.root, "rlrrr")
    val games = List(game1, game2, game3, game4, game5, game6, game7, game8, game9, game10, game11, game12, game13, game14, game15, game16)

    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )



    val winnerList = games.map(game => game.value.get.winningTeam)
    val loserList = games.map(game => game.value.get.losingTeam)

    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)

    assert(underTest.getGame(tree.root, "llrr").value.get.host === winnerList(6), "FirstRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "llrr").value.get.guest === winnerList(7), "FirstRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lrrlrlr").value.get.host === loserList(8), "FirstRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lrrlrlr").value.get.guest === loserList(9), "FirstRoundUpdateTest4")



    ////////////2nd phase//////////////////
    val game17 = underTest.getGame(updatedTree.root, "rrrlrlr") //losers in game1,2
    val game18 = underTest.getGame(updatedTree.root, "rrrlrrr") //losers in game3,4
    val game19 = underTest.getGame(updatedTree.root, "rrrrrlr") //losers in game5,6
    val game20 = underTest.getGame(updatedTree.root, "rrrrrrr") //losers in game7,8
    val game21 = underTest.getGame(updatedTree.root, "lrrlrlr") //losers in game9,10
    val game22 = underTest.getGame(updatedTree.root, "lrrlrrr") //losers in game11,12
    val game23 = underTest.getGame(updatedTree.root, "lrrrrlr") //losers in game13,14
    val game24 = underTest.getGame(updatedTree.root, "lrrrrrr") //losers in game15,16
    val game25 = underTest.getGame(updatedTree.root, "llll") //winners in game1,2
    val game26 = underTest.getGame(updatedTree.root, "lllr") //winners in game3,4
    val game27 = underTest.getGame(updatedTree.root, "llrl") //winners in game5,6
    val game28 = underTest.getGame(updatedTree.root, "llrr") //winners in game7,8
    val game29 = underTest.getGame(updatedTree.root, "rlll") //winners in game9,10
    val game30 = underTest.getGame(updatedTree.root, "rllr") //winners in game11,12
    val game31 = underTest.getGame(updatedTree.root, "rlrl") //winners in game13,14
    val game32 = underTest.getGame(updatedTree.root, "rlrr") //winners in game15,16
    val games2 = List(game17, game18, game19, game20, game21, game22, game23, game24, game25, game26, game27, game28, game29, game30, game31, game32)

    games2.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)

    assert(underTest.getGame(tree.root, "lrrlrr").value.get.guest === loserList(10), "SecondRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "lrrlrr").value.get.host === winnerList(3), "SecondRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lll").value.get.host === winnerList(0), "SecondRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lll").value.get.guest === winnerList(2), "SecondRoundUpdateTest4")
    ////////////3rd Phase///////////////////
    val game33 = underTest.getGame(updatedTree.root, "rrrlrl") //winner in game17, loser in game 29
    val game34 = underTest.getGame(updatedTree.root, "rrrlrr") //winner in game18, loser in game 30
    val game35 = underTest.getGame(updatedTree.root, "rrrrrl") //winner in game19, loser in game 31
    val game36 = underTest.getGame(updatedTree.root, "rrrrrr") //winner in game20, loser in game 32
    val game37 = underTest.getGame(updatedTree.root, "lrrlrl") //winner in game21, loser in game 25
    val game38 = underTest.getGame(updatedTree.root, "lrrlrr") //winner in game22, loser in game 26
    val game39 = underTest.getGame(updatedTree.root, "lrrrrl") //winner in game23, loser in game 27
    val game40 = underTest.getGame(updatedTree.root, "lrrrrr") //winner in game24, loser in game 28
    val game41 = underTest.getGame(updatedTree.root, "lll") //winner in game25,26
    val game42 = underTest.getGame(updatedTree.root, "llr") //winner in game27,28
    val game43 = underTest.getGame(updatedTree.root, "rll") //winner in game29,30
    val game44 = underTest.getGame(updatedTree.root, "rlr") //winner in game31,32


    val games3 = List(game33, game34, game35, game36, game37, game38, game39, game40, game41, game42, game43, game44)
    games3.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    /////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)


    assert(underTest.getGame(tree.root, "rrrrr").value.get.guest === winnerList(15), "ThirdRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rrrrr").value.get.host === winnerList(13), "ThirdRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "rl").value.get.host === winnerList(8), "ThirdRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "rl").value.get.guest === winnerList(12), "ThirdRoundUpdateTest4")

    ////////////4th Phase//////////////////

    val game45 = underTest.getGame(updatedTree.root, "rrrlr") //winner in 33,34
    val game46 = underTest.getGame(updatedTree.root, "rrrrr") //winner in 35,36
    val game47 = underTest.getGame(updatedTree.root, "lrrlr") //winner in 37,38
    val game48 = underTest.getGame(updatedTree.root, "lrrrr") //winner in 39,40
    val game49 = underTest.getGame(updatedTree.root, "ll") //winner in 41,42
    val game50 = underTest.getGame(updatedTree.root, "rl") //winner in 43,44

    val games4 = List(game45, game46, game47, game48, game49, game50)

    games4.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ///////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)

    assert(underTest.getGame(tree.root, "rrrl").value.get.guest === winnerList(9), "FourthRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rrrl").value.get.host === winnerList(2), "FourthRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lrrl").value.get.host === winnerList(10), "FourthRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lrrl").value.get.guest === winnerList(1), "FourthRoundUpdateTest4")


    ///////////5th Phase////////////////////////////
    val game51 = underTest.getGame(updatedTree.root, "rrrl") //winner in 45, loser in 41
    val game52 = underTest.getGame(updatedTree.root, "rrrr") //winner in 46, loser in 42
    val game53 = underTest.getGame(updatedTree.root, "lrrl") //winner in 47, loser in 43
    val game54 = underTest.getGame(updatedTree.root, "lrrr") //winner in 48, loser in 44

    val games5 = List(game51, game52, game53, game54)

    games5.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)


    assert(underTest.getGame(tree.root, "rrr").value.get.guest === winnerList(6), "FifthRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rrr").value.get.host === winnerList(2), "FifthRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lrr").value.get.host === winnerList(10), "FifthRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lrr").value.get.guest === winnerList(14), "FifthRoundUpdateTest4")


    ///////////6th Phase ///////////////////////
    val game55 = underTest.getGame(updatedTree.root, "rrr") //winner in 51,52
    val game56 = underTest.getGame(updatedTree.root, "lrr") //winner in 53,54


    val games6 = List(game55, game56)

    games6.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)


    assert(underTest.getGame(tree.root, "rr").value.get.guest === winnerList(2), "SixthRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rr").value.get.host === winnerList(12), "SixthRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lr").value.get.host === winnerList(4), "SixthRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lr").value.get.guest === winnerList(10), "SixthRoundUpdateTest4")


    ///////////7th Phase ///////////////////////
    val game57 = underTest.getGame(updatedTree.root, "rr") //winner in 55,loser in 50//changed
    val game58 = underTest.getGame(updatedTree.root, "lr") //winner in 56,loser in 49//changed

    val games7 = List(game57, game58)

    games7.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////7th Phase ///////////////////////


    assert(underTest.getGame(tree.root, "r").value.get.guest === winnerList(12), "SemiFinalUpdateTest1")
    assert(underTest.getGame(tree.root, "r").value.get.host === winnerList(8), "SemiFinalUpdateTest2")
    assert(underTest.getGame(tree.root, "l").value.get.host === winnerList(0), "SemiFinalUpdateTest3")
    assert(underTest.getGame(tree.root, "l").value.get.guest === winnerList(4), "SemiFinalUpdateTest4")

    val game59 = underTest.getGame(updatedTree.root, "l") //winner in 58, 49
    val game60 = underTest.getGame(updatedTree.root, "r") //winner in 57, 50
    //////////////////////////////////////
    val games8 = List(game59, game60)

    games8.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////7th Phase ///////////////////////
    val Final = underTest.getGame(updatedTree.root, "") //winner in 59,60

    {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(Final.value.get.host)
      Mockito.when(score.getLoser).thenReturn(Final.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      Final.value.get.score = Some(score)
    }


    updatedTree = underTest.updateTree(updatedTree)
    //then
    assert(Final.value.get.winningTeam === winnerList(0), "FinalUpdateTest1")
    assert(Final.value.get.losingTeam === winnerList(8), "FinalUpdateTest2")
  }


  test("UpdateTree: playing 21-team tournament till the final") {
    //given
    for (i <- 0 until 21) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = DoubleEliminationStrategy()
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lllll")
    val game2 = underTest.getGame(tree.root, "llllr")
    val game3 = underTest.getGame(tree.root, "lllrl")
    val game4 = underTest.getGame(tree.root, "lllrr")
    val game5 = underTest.getGame(tree.root, "llrll")
    val game6 = underTest.getGame(tree.root, "llrlr")
    val game7 = underTest.getGame(tree.root, "llrrl")
    val game8 = underTest.getGame(tree.root, "llrrr")
    val game9 = underTest.getGame(tree.root, "rllll")
    val game10 = underTest.getGame(tree.root, "rlllr")
    val game11 = underTest.getGame(tree.root, "rllrl")
    val game12 = underTest.getGame(tree.root, "rllrr")
    val game13 = underTest.getGame(tree.root, "rlrll")
    val game14 = underTest.getGame(tree.root, "rlrlr")
    val game15 = underTest.getGame(tree.root, "rlrrl")
    val game16 = underTest.getGame(tree.root, "rlrrr")
    val games = List(game1, game2, game3, game4, game5, game6, game7, game8, game9, game10, game11, game12, game13, game14, game15, game16)
    val normalMatches = List(game4, game5, game6, game7, game8)
    normalMatches.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    val winnerList = games.map(game => game.value.get.winningTeam)
    val loserList = normalMatches.map(game => game.value.get.losingTeam)

    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)

    assert(underTest.getGame(tree.root, "llrr").value.get.host === winnerList(6), "FirstRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "llrr").value.get.guest === winnerList(7), "FirstRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lrrlrlr").value.get.host === None, "FirstRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lrrlrlr").value.get.guest === None, "FirstRoundUpdateTest4")
    assert(underTest.getGame(tree.root, "rrrlrrr").value.get.host === None, "FirstRoundUpdateTest5")
    assert(underTest.getGame(tree.root, "rrrlrrr").value.get.guest === loserList(0), "FirstRoundUpdateTest6")



    ////////////2nd phase//////////////////
    val game17 = underTest.getGame(updatedTree.root, "rrrlrlr") //losers in game1,2 -none vs none
    val game18 = underTest.getGame(updatedTree.root, "rrrlrrr") //losers in game3,4 -loserList(0) vs none
    val game19 = underTest.getGame(updatedTree.root, "rrrrrlr") //losers in game5,6
    val game20 = underTest.getGame(updatedTree.root, "rrrrrrr") //losers in game7,8
    val game21 = underTest.getGame(updatedTree.root, "lrrlrlr") //losers in game9,10 - none vs none
    val game22 = underTest.getGame(updatedTree.root, "lrrlrrr") //losers in game11,12
    val game23 = underTest.getGame(updatedTree.root, "lrrrrlr") //losers in game13,14
    val game24 = underTest.getGame(updatedTree.root, "lrrrrrr") //losers in game15,16
    val game25 = underTest.getGame(updatedTree.root, "llll") //winners in game1,2
    val game26 = underTest.getGame(updatedTree.root, "lllr") //winners in game3,4
    val game27 = underTest.getGame(updatedTree.root, "llrl") //winners in game5,6
    val game28 = underTest.getGame(updatedTree.root, "llrr") //winners in game7,8
    val game29 = underTest.getGame(updatedTree.root, "rlll") //winners in game9,10
    val game30 = underTest.getGame(updatedTree.root, "rllr") //winners in game11,12
    val game31 = underTest.getGame(updatedTree.root, "rlrl") //winners in game13,14
    val game32 = underTest.getGame(updatedTree.root, "rlrr") //winners in game15,16
    val games2 = List(game17, game18, game19, game20, game21, game22, game23, game24, game25, game26, game27, game28, game29, game30, game31, game32)

    games2.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)


    assert(underTest.getGame(tree.root, "lrrlrr").value.get.guest === None, "SecondRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "lrrlrr").value.get.host === winnerList(3), "SecondRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lll").value.get.host === winnerList(0), "SecondRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lll").value.get.guest === winnerList(2), "SecondRoundUpdateTest4")
    assert(underTest.getGame(tree.root, "rrrlrl").value.get.guest === None, "SecondRoundUpdateTest5")
    assert(underTest.getGame(tree.root, "rrrlrl").value.get.host === winnerList(9), "SecondRoundUpdateTest6")
    assert(underTest.getGame(tree.root, "rrrlrr").value.get.host === winnerList(11), "SecondRoundUpdateTest7")
    assert(underTest.getGame(tree.root, "rrrlrr").value.get.guest === loserList(0), "SecondRoundUpdateTest8")

    ////////////3rd Phase///////////////////
    val game33 = underTest.getGame(updatedTree.root, "rrrlrl") //winner in game17, loser in game 29
    val game34 = underTest.getGame(updatedTree.root, "rrrlrr") //winner in game18, loser in game 30
    val game35 = underTest.getGame(updatedTree.root, "rrrrrl") //winner in game19, loser in game 31
    val game36 = underTest.getGame(updatedTree.root, "rrrrrr") //winner in game20, loser in game 32
    val game37 = underTest.getGame(updatedTree.root, "lrrlrl") //winner in game21, loser in game 25
    val game38 = underTest.getGame(updatedTree.root, "lrrlrr") //winner in game22, loser in game 26
    val game39 = underTest.getGame(updatedTree.root, "lrrrrl") //winner in game23, loser in game 27
    val game40 = underTest.getGame(updatedTree.root, "lrrrrr") //winner in game24, loser in game 28
    val game41 = underTest.getGame(updatedTree.root, "lll") //winner in game25,26
    val game42 = underTest.getGame(updatedTree.root, "llr") //winner in game27,28
    val game43 = underTest.getGame(updatedTree.root, "rll") //winner in game29,30
    val game44 = underTest.getGame(updatedTree.root, "rlr") //winner in game31,32


    val games3 = List(game33, game34, game35, game36, game37, game38, game39, game40, game41, game42, game43, game44)
    games3.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    /////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)


    assert(underTest.getGame(tree.root, "rrrrr").value.get.guest === winnerList(15), "ThirdRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rrrrr").value.get.host === winnerList(13), "ThirdRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "rl").value.get.host === winnerList(8), "ThirdRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "rl").value.get.guest === winnerList(12), "ThirdRoundUpdateTest4")

    ////////////4th Phase//////////////////

    val game45 = underTest.getGame(updatedTree.root, "rrrlr") //winner in 33,34
    val game46 = underTest.getGame(updatedTree.root, "rrrrr") //winner in 35,36
    val game47 = underTest.getGame(updatedTree.root, "lrrlr") //winner in 37,38
    val game48 = underTest.getGame(updatedTree.root, "lrrrr") //winner in 39,40
    val game49 = underTest.getGame(updatedTree.root, "ll") //winner in 41,42
    val game50 = underTest.getGame(updatedTree.root, "rl") //winner in 43,44

    val games4 = List(game45, game46, game47, game48, game49, game50)

    games4.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    ///////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)

    assert(underTest.getGame(tree.root, "rrrl").value.get.guest === winnerList(9), "FourthRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rrrl").value.get.host === winnerList(2), "FourthRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lrrl").value.get.host === winnerList(10), "FourthRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lrrl").value.get.guest === winnerList(1), "FourthRoundUpdateTest4")


    ///////////5th Phase////////////////////////////
    val game51 = underTest.getGame(updatedTree.root, "rrrl") //winner in 45, loser in 41
    val game52 = underTest.getGame(updatedTree.root, "rrrr") //winner in 46, loser in 42
    val game53 = underTest.getGame(updatedTree.root, "lrrl") //winner in 47, loser in 43
    val game54 = underTest.getGame(updatedTree.root, "lrrr") //winner in 48, loser in 44

    val games5 = List(game51, game52, game53, game54)

    games5.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)


    assert(underTest.getGame(tree.root, "rrr").value.get.guest === winnerList(6), "FifthRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rrr").value.get.host === winnerList(2), "FifthRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lrr").value.get.host === winnerList(10), "FifthRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lrr").value.get.guest === winnerList(14), "FifthRoundUpdateTest4")


    ///////////6th Phase ///////////////////////
    val game55 = underTest.getGame(updatedTree.root, "rrr") //winner in 51,52
    val game56 = underTest.getGame(updatedTree.root, "lrr") //winner in 53,54
    val games6 = List(game55, game56)

    games6.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)


    assert(underTest.getGame(tree.root, "rr").value.get.guest === winnerList(2), "SixthRoundUpdateTest1")
    assert(underTest.getGame(tree.root, "rr").value.get.host === winnerList(12), "SixthRoundUpdateTest2")
    assert(underTest.getGame(tree.root, "lr").value.get.host === winnerList(4), "SixthRoundUpdateTest3")
    assert(underTest.getGame(tree.root, "lr").value.get.guest === winnerList(10), "SixthRoundUpdateTest4")


    ///////////7th Phase ///////////////////////
    val game57 = underTest.getGame(updatedTree.root, "rr") //winner in 55,loser in 50//changed
    val game58 = underTest.getGame(updatedTree.root, "lr") //winner in 56,loser in 49//changed

    val games7 = List(game57, game58)

    games7.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////7th Phase ///////////////////////


    assert(underTest.getGame(tree.root, "r").value.get.guest === winnerList(12), "SemiFinalUpdateTest1")
    assert(underTest.getGame(tree.root, "r").value.get.host === winnerList(8), "SemiFinalUpdateTest2")
    assert(underTest.getGame(tree.root, "l").value.get.host === winnerList(0), "SemiFinalUpdateTest3")
    assert(underTest.getGame(tree.root, "l").value.get.guest === winnerList(4), "SemiFinalUpdateTest4")

    val game59 = underTest.getGame(updatedTree.root, "l") //winner in 58, 49
    val game60 = underTest.getGame(updatedTree.root, "r") //winner in 57, 50
    //////////////////////////////////////
    val games8 = List(game59, game60)

    games8.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////7th Phase ///////////////////////
    val Final = underTest.getGame(updatedTree.root, "") //winner in 59,60

    val score: VolleyballScore = mock[VolleyballScore]
    Mockito.when(score.getWinner).thenReturn(Final.value.get.host)
    Mockito.when(score.getLoser).thenReturn(Final.value.get.guest)
    Mockito.when(score.isMatchFinished).thenReturn(true)
    Final.value.get.score = Some(score)

    updatedTree = underTest.updateTree(updatedTree)
    //then
    assert(Final.value.get.winningTeam === winnerList(0), "FinalUpdateTest1")
    assert(Final.value.get.losingTeam === winnerList(8), "FinalUpdateTest2")
  }


}

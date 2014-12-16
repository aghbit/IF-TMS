
import models.strategy.Tree.EliminationTree
import models.strategy.VSet
import models.strategy.scores.VolleyballScore
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
    tree = underTest.populateTree(tree,listOfTeams)
    //when
    val testDrawing1:Boolean = underTest.areAllTeamsSet(tree,listOfTeams)
    val testDrawing2:Boolean = underTest.is2ndand4thQuarterEmpty(tree.root)
    //then
    assert(testDrawing1,"populateTree: 2^n teams1")
    assert(testDrawing2,"populateTree: 2^n teams2")
    assert(tree.root.value==null,"populateTree: 2^n teams3")
    assert(tree.root.left.left.left.value==null,"populateTree: 2^n teams3")
    assert(tree.root.left.left.left.left.value!=null,"populateTree: 2^n teams3")
    assert(tree.root.left.left.left==underTest.getGame(tree.root,"lll"),"populateTree: 2^n teams4")
    assert(tree.root.left.left.left.left==underTest.getGame(tree.root,"llll"),"populateTree: 2^n teams5")
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


  test("UpdateTree: Updating first-match in 8-team tournament"){
    //given
    for(i<-0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    val game1 = underTest.getGame(tree.root,"lll")
    val game2 = underTest.getGame(tree.root,"llr")

    val score:VolleyballScore = mock[VolleyballScore]
    val score2:VolleyballScore = mock[VolleyballScore]
    Mockito.when(score.isEnded).thenReturn(true)
    game1.value.scoreHost=score
    game2.value.scoreGuest=score
    game1.value.scoreGuest=score2
    game2.value.scoreHost=score2
    val winner1 = game1.value.winningTeam
    val winner2 = game2.value.winningTeam
    val loser1 = game1.value.losingTeam
    val loser2 = game2.value.losingTeam

    val updatedTree = underTest.updateTree(tree)
    val newGame = underTest.getGame(updatedTree.root,"ll")
    val newGameLosers = underTest.getGame(updatedTree.root,"rrr")
    //then
    assert(newGame.value.host===winner1,"UpdateTree: Updating first-match in 8-team tournament1")
    assert(newGame.value.guest===winner2,"UpdateTree: Updating first-match in 8-team tournament2")
    assert(newGameLosers.value.host===loser1,"UpdateTree: Updating first-match in 8-team tournament3")
    assert(newGameLosers.value.guest===loser2,"UpdateTree: Updating first-match in 8-team tournament4")
  }

  test("UpdateTree: playing 8-team tournament, not playing all matches"){
    //given
    for(i<-0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
    tree = underTest.populateTree(tree,listOfTeams)

    //when
    //score Part
    val score:VolleyballScore = mock[VolleyballScore]
    val score2:VolleyballScore = mock[VolleyballScore]
    Mockito.when(score.isEnded).thenReturn(true)


////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root,"lll")
    val game2 = underTest.getGame(tree.root,"llr")
    val game3 = underTest.getGame(tree.root,"rll")
    val game4 = underTest.getGame(tree.root,"rlr")
    val games = List(game1,game2,game3,game4)

    games.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    val winnerList = games.map(game=>game.value.winningTeam)
    val loserList = games.map(game=>game.value.losingTeam)
///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root,"ll")
    val game6 = underTest.getGame(tree.root,"rrr")
    val games2 = List(game5,game6)

    games2.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))


    updatedTree = underTest.updateTree(updatedTree)
    val newGame1 = underTest.getGame(updatedTree.root,"l")
    val newGame2 = underTest.getGame(updatedTree.root,"r")
    val newGame3 = underTest.getGame(updatedTree.root,"rr")
    val newGame4 = underTest.getGame(updatedTree.root,"lr")
//////////////////////////////////////

    //then
    assert(newGame1.value===null,"UpdateTree: playing 8-team tournament, not playing all matches1")
    assert(newGame2.value===null,"UpdateTree: playing 8-team tournament, not playing all matches2")
    assert(newGame3.value===null,"UpdateTree: playing 8-team tournament, not playing all matches3")
    assert(newGame4.value===null,"UpdateTree: playing 8-team tournament, not playing all matches4")
  }

  test("UpdateTree: playing 8-team tournament till the final"){
    //given
    for(i<-0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
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
    val game1 = underTest.getGame(tree.root,"lll")
    val game2 = underTest.getGame(tree.root,"llr")
    val game3 = underTest.getGame(tree.root,"rll")
    val game4 = underTest.getGame(tree.root,"rlr")
    val games = List(game1,game2,game3,game4)

    games.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))



    val winnerList = games.map(game=>game.value.winningTeam)
    val loserList = games.map(game=>game.value.losingTeam)
    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(updatedTree.root,"ll")
    val game6 = underTest.getGame(updatedTree.root,"lrr")
    val game7 = underTest.getGame(updatedTree.root,"rl")
    val game8 = underTest.getGame(updatedTree.root,"rrr")
    val games2 = List(game5,game6,game7,game8)

    games2.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    ////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////3rd Phase///////////////////
    val game9 = underTest.getGame(updatedTree.root,"lr")
    val game10 = underTest.getGame(updatedTree.root,"rr")
    val games3 = List(game9,game10)


    games3.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    /////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////Semi-finals//////////////////
    val game11 = underTest.getGame(updatedTree.root,"l")
    val game12 = underTest.getGame(updatedTree.root,"r")
    val games4 = List(game11,game12)

    games4.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    ///////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////Final////////////////////////////
    val game13 = underTest.getGame(updatedTree.root,"")
    val games5 = List(game13)

    games5.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)

    val Final = underTest.getGame(updatedTree.root,"")

    //////////////////////////////////////

    //then
    assert(Final.value.winningTeam===winnerList(0),"UpdateTree: playing 8-team tournament till the final1")
    assert(Final.value.losingTeam===winnerList(2),"UpdateTree: playing 8-team tournament till the final2")
  }

  test("UpdateTree: playing 5-team tournament till the final Semis Phase"){
    //given
    for(i<-0 until 5) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
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
    val game1 = underTest.getGame(tree.root,"lll")
    val game2 = underTest.getGame(tree.root,"llr")
    val game3 = underTest.getGame(tree.root,"rll")
    val game4 = underTest.getGame(tree.root,"rlr")
    val games = List(game1,game2,game3,game4)

    game2.value.scoreHost=score
    game2.value.scoreGuest=score2







    //val winnerList = games.map(game=>game.value.winningTeam)
    val loserList = game2.value.losingTeam

    val winnerList = games.map(game=>game.value.winningTeam)
    ///////////////////////////////////////
    var updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(updatedTree.root,"ll")
    val game6 = underTest.getGame(updatedTree.root,"lrr")
    val game7 = underTest.getGame(updatedTree.root,"rl")
    val game8 = underTest.getGame(updatedTree.root,"rrr")
    val games2 = List(game5,game6,game7,game8)

    games2.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    ////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////3rd Phase///////////////////
    val game9 = underTest.getGame(updatedTree.root,"lr")
    val game10 = underTest.getGame(updatedTree.root,"rr")
    val games3 = List(game9,game10)


    games3.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    /////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ////////////Semi-finals//////////////////
    val game11 = underTest.getGame(updatedTree.root,"l")
    val game12 = underTest.getGame(updatedTree.root,"r")
    val games4 = List(game11,game12)

    games4.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))

    ///////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)
    ///////////Final////////////////////////////
    val game13 = underTest.getGame(updatedTree.root,"")
    val games5 = List(game13)

    games5.foreach(game => (game.value.scoreHost=score, game.value.scoreGuest=score2))
    ////////////////////////////////////////////
    updatedTree = underTest.updateTree(updatedTree)

    val Final = underTest.getGame(updatedTree.root,"")

    //////////////////////////////////////

    //then
    assert(Final.value.winningTeam===winnerList(0),"UpdateTree: playing 8-team tournament till the final1")
    assert(Final.value.losingTeam===winnerList(2),"UpdateTree: playing 8-team tournament till the final2")
  }

  test("UpdateTree: playing 8-team tournament till the final Final Phase"){
    //given
    for(i<-0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest=DoubleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree()
    tree = underTest.populateTree(tree,listOfTeams)

    //when

    //then
  }




  test("UpdateTree: updating first-match in 13-team tournament"){
    //given
    for(i<-0 until 24) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }

    //when

    //then

  }

  test("UpdateTree: playing 13-team tournament till the final"){
    //given
    for(i<-0 until 24) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }

    //when

    //then

  }

}

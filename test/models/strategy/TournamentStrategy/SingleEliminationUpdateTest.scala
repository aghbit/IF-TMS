
import models.Game.{Game, EliminationTree}
import models.strategy.scores.VolleyballScore
import models.strategy.strategies.SingleEliminationStrategy
import models.team.Team
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import reactivemongo.bson.BSONObjectID

/**
 * Created by ludwik on 17.12.14.
 */
@RunWith(classOf[JUnitRunner])
class SingleEliminationUpdateTest extends FunSuite with BeforeAndAfter with MockitoSugar {
  var underTest: SingleEliminationStrategy = _
  var listOfTeams: List[Team] = _
  var tree: EliminationTree = _

  before {

  }


  test("UpdateTree: playing 8-team tournament, not playing all matches") {
    //given

    listOfTeams = List()
    for (i <- 0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
    }
    underTest = SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "ll")
    val game2 = underTest.getGame(tree.root, "lr")
    val game3 = underTest.getGame(tree.root, "rl")
    val game4 = underTest.getGame(tree.root, "rr")
    val games = List(game1, game2, game3, game4)
    //score Part

    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    val updatedTree = underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root, "l")
    val game6 = underTest.getGame(tree.root, "r")
    val newGame3 = updatedTree.root
    //then
    assert(newGame3.value === None, "UpdateTree: playing 8-team tournament without winner")
    assert(game5.value != None, "UpdateTree: playing 8-team tournament without winner")
    assert(game6.value != None, "UpdateTree: playing 8-team tournament without winner")

  }

  test("UpdateTree: playing 8-team tournament, checking one update") {
    //given
    listOfTeams = List()
    for (i <- 0 until 8) {
      val team = mock[Team]
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team " + i)
      listOfTeams = team :: listOfTeams
    }
    underTest = SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)
    //when

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "ll")
    val game2 = underTest.getGame(tree.root, "lr")
    val game3 = underTest.getGame(tree.root, "rl")
    val game4 = underTest.getGame(tree.root, "rr")
    val games = List(game1, game2, game3, game4)
    // All hosts win
    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    underTest.updateTree(tree) //update tree
    //Now getting games that are higher on our matches edder
    val game5 = underTest.getGame(tree.root, "l")
    val game6 = underTest.getGame(tree.root, "r")


    //checking if updated fields arent empty
    val condition0 = game5.value.get.host != None && game5.value.get.guest != None && game1.value.get.host != None
    // checking if updated team is the team should have been updated  (comparing names)
    val condition1 = game5.value.get.host.toString().equals(game1.value.get.host.toString())
    val condition2 = game5.value.get.guest.toString().equals(game2.value.get.host.toString())



    assert(game1.value.get.host != None, "UpdateTree: playing 8-team tournament, testing one check")
    assert(tree.root.left.get.value != None, "UpdateTree: playing 8-team tournament, testing one check")
    assert(tree.root.value == None, "UpdateTree: playing 8-team tournament, testing one check")
    assert(condition0, "UpdateTree: playing 8-team tournament, testing one check")
    assert(condition1, "UpdateTree: playing 8-team tournament, testing one check")
    assert(condition2, "UpdateTree: playing 8-team tournament, testing one check")
  }
  test("UpdateTree: playing 8-team tournament, checking the winner") {
    //given
    listOfTeams = List()

    for (i <- 0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team " + i)
    }
    underTest = SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "ll")
    val game2 = underTest.getGame(tree.root, "lr")
    val game3 = underTest.getGame(tree.root, "rl")
    val game4 = underTest.getGame(tree.root, "rr")
    var games = List(game1, game2, game3, game4)
    /// All hosts win
    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root, "l")
    val game6 = underTest.getGame(tree.root, "r")
    //All hosts win again
    games = List(game5, game6)
    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    val finalmatch = underTest.updateTree(tree).root

    //then
    assert(finalmatch != None, "Update Tree: FinalMatch is null")
    val condition = finalmatch.value.get.host == game1.value.get.host //checking first team in final
    val condition2 = finalmatch.value.get.guest == game3.value.get.host //checking the second team

    assert(condition, "Update Tree: 8 teams match, checking the final")
    assert(condition2, "Update Tree: 8 teams match, checking the final")
  }
  test("UpdateTree: playing 8-team tournament, checking the winner 2") {

    //given

    listOfTeams = List()
    for (i <- 0 until 8) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team " + i)
    }
    underTest = SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "ll")
    val game2 = underTest.getGame(tree.root, "lr")
    val game3 = underTest.getGame(tree.root, "rl")
    val game4 = underTest.getGame(tree.root, "rr")
    var games = List(game1, game2, game3, game4)
    // 1-wins host, 2 -wins guest
    var winList = List(2, 1, 1, 2)
    var inputek = games zip winList
    // for every game in games Ends match with winning score for host (1) or for guest(2)
    inputek.foreach(e => {
      val (game, win) = e
      val score: VolleyballScore = mock[VolleyballScore]
      if (1 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
        Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      } else if (2 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.host)
        Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      }
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    underTest.updateTree(tree)
    ////////////2nd phase//////////////////
    val game5 = underTest.getGame(tree.root, "l")
    val game6 = underTest.getGame(tree.root, "r")
    games = List(game5, game6)
    winList = List(2, 2) //the same as above, win only guests
    inputek = games zip winList
    inputek.foreach(e => {
      val (game: Game, win: Int) = e
      val score: VolleyballScore = mock[VolleyballScore]
      if (win == 1) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
        Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      } else if (win == 2) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.host)
        Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      }
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    underTest.updateTree(tree)

    val finalmatch = underTest.updateTree(tree).root

    //then

    val condition = finalmatch.value.get.host == game2.value.get.host //checking first team in final
    val condition2 = finalmatch.value.get.guest == game4.value.get.guest //checking the second team

    assert(condition, "Update Tree: 8 teams match, checking the final")
    assert(condition2, "Update Tree: 8 teams match, checking the final")
  }
  test("UpdateTree: playing 12-team tournament, 1 phase") {

    //given

    listOfTeams = List()
    for (i <- 0 until 12) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team " + i)
    }
    underTest = SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lll")
    val game2 = underTest.getGame(tree.root, "llr")
    val game3 = underTest.getGame(tree.root, "lrl")
    val game4 = underTest.getGame(tree.root, "lrr")
    val game5 = underTest.getGame(tree.root, "rll")
    val game6 = underTest.getGame(tree.root, "rlr")
    val game7 = underTest.getGame(tree.root, "rrl")
    val game8 = underTest.getGame(tree.root, "rrr")

    val games = List(game1, game2, game3, game4, game5, game6, game7, game8)
    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      Mockito.when(score.getLoser).thenReturn(game.value.get.host)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )


    underTest.updateTree(tree) // updating matches

    val game9 = underTest.getGame(tree.root, "ll")
    val game10 = underTest.getGame(tree.root, "lr")
    val game11 = underTest.getGame(tree.root, "rl")
    val game12 = underTest.getGame(tree.root, "rr")

    //Conditions below checking if updated match consits of teams that have won
    // Its the easiest way
    val condition1 = game9.value.get.host == game1.value.get.guest && game9.value.get.guest == game2.value.get.guest
    val condition2 = game10.value.get.host == game3.value.get.guest && game10.value.get.guest == game4.value.get.guest
    val condition3 = game11.value.get.host == game5.value.get.host && game11.value.get.guest == game6.value.get.host

    assert(condition1, "Update Tree: 12 teams, update after 1 phase")
    assert(condition2, "Update Tree: 12 teams, update after 1 phase")
    assert(condition3, "Update Tree: 12 teams, update after 1 phase")
  }
  test("UpdateTree: playing 12-team tournament,update to final") {

    //given

    listOfTeams = List()
    for (i <- 0 until 12) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team " + i)
    }
    underTest = SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lll")
    val game2 = underTest.getGame(tree.root, "llr")
    val game3 = underTest.getGame(tree.root, "lrl")
    val game4 = underTest.getGame(tree.root, "lrr")
    val game5 = underTest.getGame(tree.root, "rll")
    val game6 = underTest.getGame(tree.root, "rlr")
    val game7 = underTest.getGame(tree.root, "rrl")
    val game8 = underTest.getGame(tree.root, "rrr")
    var games = List(game1, game2, game3, game4, game5, game6, game7, game8)
    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      Mockito.when(score.getLoser).thenReturn(game.value.get.host)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    var updatedTree = underTest.updateTree(tree) // updating matches
    val game9 = underTest.getGame(tree.root, "ll")
    val game10 = underTest.getGame(tree.root, "lr")
    val game11 = underTest.getGame(tree.root, "rl")
    val game12 = underTest.getGame(tree.root, "rr")
    var winList = List(1, 2, 1, 2)
    games = List(game9, game10, game11, game12)
    var inputek = games zip winList

    inputek.foreach(e => {
      val (game, win) = e
      val score: VolleyballScore = mock[VolleyballScore]
      if (1 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
        Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      } else if (2 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.host)
        Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      }
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    updatedTree = underTest.updateTree(tree)
    val game13 = underTest.getGame(tree.root, "l")
    val game14 = underTest.getGame(tree.root, "r")
    winList = List(2, 2)
    games = List(game13, game14)
    inputek = games zip winList
    inputek.foreach(e => {
      val (game, win) = e
      val score: VolleyballScore = mock[VolleyballScore]
      if (1 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
        Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      } else if (2 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.host)
        Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      }
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    underTest.updateTree(tree)
    val condition = tree.root.value.get.host == game4.value.get.guest
    val condition2 = tree.root.value.get.guest == game8.value.get.host
    assert(condition, "Update Tree: 12 teams, update to final")
    assert(condition2, "Update Tree: 12 teams, update to final")

  }
  test("UpdateTree: playing 16-team tournament, whole match simulation") {

    //given

    listOfTeams = List()
    for (i <- 0 until 16) {
      val team = mock[Team]
      listOfTeams = team :: listOfTeams
      Mockito.when(team._id).thenReturn(BSONObjectID.generate)
      Mockito.when(team.name).thenReturn("team " + i)
    }
    underTest = SingleEliminationStrategy(listOfTeams)
    tree = underTest.generateTree(listOfTeams)
    tree = underTest.drawTeamsInTournament(tree, listOfTeams)

    //when
    //score Part


    ////////////1st phase/////////////////
    val game1 = underTest.getGame(tree.root, "lll")
    val game2 = underTest.getGame(tree.root, "llr")
    val game3 = underTest.getGame(tree.root, "lrl")
    val game4 = underTest.getGame(tree.root, "lrr")
    val game5 = underTest.getGame(tree.root, "rll")
    val game6 = underTest.getGame(tree.root, "rlr")
    val game7 = underTest.getGame(tree.root, "rrl")
    val game8 = underTest.getGame(tree.root, "rrr")

    var games = List(game1, game2, game3, game4, game5, game6, game7, game8)


    games.foreach(game => {
      val score: VolleyballScore = mock[VolleyballScore]
      Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )
    var updatedTree = underTest.updateTree(tree) // updating matches
    //Right now play : 1 vs 3, 5 vs 7, 9 vs 11, 13 vs 15
    val game9 = underTest.getGame(tree.root, "ll")
    val game10 = underTest.getGame(tree.root, "lr")
    val game11 = underTest.getGame(tree.root, "rl")
    val game12 = underTest.getGame(tree.root, "rr")

    var winList = List(2, 1, 2, 2)
    games = List(game9, game10, game11, game12)
    var inputek = games zip winList

    inputek.foreach(e => {
      val (game, win) = e
      val score: VolleyballScore = mock[VolleyballScore]
      if (1 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
        Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      } else if (2 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.host)
        Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      }
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    updatedTree = underTest.updateTree(tree)
    //right now play: 3 vs 5 and 11 vs 15
    val game13 = underTest.getGame(tree.root, "l")
    val game14 = underTest.getGame(tree.root, "r")
    winList = List(2, 2)
    games = List(game13, game14)
    inputek = games zip winList
    inputek.foreach(e => {
      val (game, win) = e
      val score: VolleyballScore = mock[VolleyballScore]
      if (1 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.guest)
        Mockito.when(score.getWinner).thenReturn(game.value.get.host)
      } else if (2 == win) {
        Mockito.when(score.getLoser).thenReturn(game.value.get.host)
        Mockito.when(score.getWinner).thenReturn(game.value.get.guest)
      }
      Mockito.when(score.isMatchFinished).thenReturn(true)
      game.value.get.score = Some(score)
    }
    )

    updatedTree = underTest.updateTree(tree)
    //in final should be 3 and 15 team
    val condition0 = tree.root.left.get.value.get.host == game2.value.get.host && tree.root.left.get.value.get.guest == game3.value.get.host
    val condition1 = tree.root.value.get.host == game3.value.get.host && tree.root.value.get.guest == game8.value.get.host

    assert(condition0, "Update Tree: 16 teams match, checking semi final match")
    assert(condition1, "Update Tree: 16 teams match, checking final teams")
  }

}


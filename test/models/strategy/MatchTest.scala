package models.strategy

import models.strategy.scores.MatchNotFinishedException
import models.team.Team
import org.mockito.Mockito
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Rafal on 2014-12-11.
 */
class MatchTest extends FunSuite with BeforeAndAfter with MockitoSugar{
  var underTest:Match = _
  var host:Team = _
  var guest:Team = _
  var hostId:BSONObjectID = _
  var guestId:BSONObjectID = _
  var score:Score = _


  before{
    score = mock[Score]
    host = mock[Team]
    guest= mock[Team]
    hostId = BSONObjectID.generate
    guestId = BSONObjectID.generate
    Mockito.when(host._id).thenReturn(hostId)
    Mockito.when(guest._id).thenReturn(guestId)
    underTest=Match(Option(host),Option(guest))
  }



  test("WinningTeam test: hostWins"){

    //given
    Mockito.when(score.getWinner).thenReturn(Some(hostId))
    Mockito.when(score.getLoser).thenReturn(Some(guestId))

    underTest.score=Some(score)

    //when

    //then
    assert(underTest.losingTeam.get===guestId,"WinningTeam test: hostWins1")
    assert(underTest.winningTeam.get===hostId,"WinningTeam test: hostWins2")
  }

  test("WinningTeam test: guestWins"){

    //given
    Mockito.when(score.getWinner).thenReturn(Some(guestId))
    Mockito.when(score.getLoser).thenReturn(Some(hostId))

    underTest.score=Some(score)

    //when

    //then
    assert(underTest.losingTeam.get===hostId,"WinningTeam test: guestWins1")
    assert(underTest.winningTeam.get===guestId,"WinningTeam test: guestWins2")
  }

  test("WinningTeam test: one team is null"){

    //given
    underTest=Match(Option(host),None)

    //when

    //then
    assert(underTest.losingTeam===None,"WinningTeam test: one team is null1")
    assert(underTest.winningTeam.get===hostId,"WinningTeam test: one team is null2")
  }

  test("WinningTeam test: two teams are null"){

    //given
    underTest=Match(None,None)

    //when

    //then
    assert(underTest.losingTeam===None,"WinningTeam test: two teams are null1")
    assert(underTest.winningTeam===None,"WinningTeam test: two teams are null2")
  }


}

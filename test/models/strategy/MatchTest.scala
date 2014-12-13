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
  var scoreHost:Score = _
  var scoreGuest:Score = _

  before{
    scoreHost = mock[Score]
    scoreGuest = mock[Score]
    host = mock[Team]
    guest= mock[Team]
    hostId = BSONObjectID.generate
    guestId = BSONObjectID.generate
    Mockito.when(host._id).thenReturn(hostId)
    Mockito.when(guest._id).thenReturn(guestId)
    underTest=Match(host,guest)
  }

  test("WinningTeam test: failure"){

    //given

    //when

    //then
    intercept[MatchNotFinishedException]{
      underTest.winningTeam
    }
  }

  test("WinningTeam test: hostWins"){

    //given
    Mockito.when(scoreHost.>(scoreGuest)).thenReturn(true)
    Mockito.when(scoreGuest.<(scoreHost)).thenReturn(false)
    underTest.scoreHost = scoreHost
    underTest.scoreGuest = scoreGuest

    //when

    //then
    assert(underTest.winningTeam===hostId,"WinningTeam test1")
  }

  test("WinningTeam test: guestWins"){

    //given
    Mockito.when(scoreHost.<(scoreGuest)).thenReturn(true)
    Mockito.when(scoreGuest.>(scoreHost)).thenReturn(false)
    underTest.scoreHost = scoreHost
    underTest.scoreGuest = scoreGuest

    //when

    //then
    assert(underTest.winningTeam===guestId,"WinningTeam test2")
  }
}

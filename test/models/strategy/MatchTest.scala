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
//  var underTest:Match = _
//  var host:Team = _
//  var guest:Team = _
//  var hostId:BSONObjectID = _
//  var guestId:BSONObjectID = _
//  var scoreHost:Score = _
//  var scoreGuest:Score = _
//
//  before{
//    scoreHost = mock[Score]
//    scoreGuest = mock[Score]
//    host = mock[Team]
//    guest= mock[Team]
//    hostId = BSONObjectID.generate
//    guestId = BSONObjectID.generate
//    Mockito.when(host._id).thenReturn(hostId)
//    Mockito.when(guest._id).thenReturn(guestId)
//    underTest=Match(host,guest)
//  }
//
//  test("WinningTeam test: failure"){
//
//    //given
//
//    //when
//
//    //then
//    intercept[MatchNotFinishedException]{
//      underTest.winningTeam
//    }
//  }
//
//  test("WinningTeam test: hostWins"){
//
//    //given
//    Mockito.when(scoreHost.>(scoreGuest)).thenReturn(true)
//    Mockito.when(scoreHost.<(scoreGuest)).thenReturn(false)
//    Mockito.when(scoreGuest.<(scoreHost)).thenReturn(true)
//    Mockito.when(scoreGuest.>(scoreHost)).thenReturn(false)
//
//
//    underTest.scoreHost = scoreHost
//    underTest.scoreGuest = scoreGuest
//
//    //when
//
//    //then
//    assert(underTest.losingTeam===guestId,"WinningTeam test: hostWins1")
//    assert(underTest.winningTeam===hostId,"WinningTeam test: hostWins2")
//  }
//
//  test("WinningTeam test: guestWins"){
//
//    //given
//    Mockito.when(scoreHost.<(scoreGuest)).thenReturn(true)
//    Mockito.when(scoreHost.>(scoreGuest)).thenReturn(false)
//    Mockito.when(scoreGuest.>(scoreHost)).thenReturn(true)
//    Mockito.when(scoreGuest.<(scoreHost)).thenReturn(false)
//    underTest.scoreHost = scoreHost
//    underTest.scoreGuest = scoreGuest
//
//    //when
//
//    //then
//    assert(underTest.losingTeam===hostId,"WinningTeam test: guestWins1")
//    assert(underTest.winningTeam===guestId,"WinningTeam test: guestWins2")
//  }
//
//  test("WinningTeam test: one team is null"){
//
//    //given
//    underTest=Match(host,null)
//
//    //when
//
//    //then
//    assert(underTest.losingTeam===null,"WinningTeam test: one team is null1")
//    assert(underTest.winningTeam===hostId,"WinningTeam test: one team is null2")
//  }
//
//  test("WinningTeam test: two teams are null"){
//
//    //given
//    underTest=Match(null,null)
//
//    //when
//
//    //then
//    assert(underTest.losingTeam===null,"WinningTeam test: two teams are null1")
//    assert(underTest.winningTeam===null,"WinningTeam test: two teams are null2")
//  }


}

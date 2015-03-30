package models.tournaments.tournamentstate

import java.util

import models.tournament.tournamentstate.TournamentStaff
import models.user.User
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
@RunWith(classOf[JUnitRunner])
class StaffTest extends FunSuite with MockitoSugar with BeforeAndAfter {
  var refs: ListBuffer[User] = _
  var memberRefs: util.ArrayList[BSONObjectID] = new util.ArrayList[BSONObjectID]()
  var staff: TournamentStaff = new TournamentStaff(mock[BSONObjectID], memberRefs)

  before {
    refs = ListBuffer(mock[User], mock[User], mock[User], mock[User], mock[User], mock[User])
    refs.foreach(user => Mockito.when(user._id).thenReturn(BSONObjectID.generate))
  }

  test("addReferee: test") {
    // given
    staff.addReferee(refs(0))

    // when
    val containsReferee = staff.contains(refs(0))

    // then
    assert(containsReferee, "addReferee: test")
  }

  test("removeReferee: test") {
    //given
    staff.addReferee(refs(0))
    staff.removeReferee(refs(0))

    //when
    val containsReferee = staff.contains(refs(0))

    //then
    assert(!containsReferee, "removeReferee: test")
  }

}

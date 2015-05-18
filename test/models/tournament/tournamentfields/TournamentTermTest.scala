package models.tournament.tournamentfields

import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
 * Created by szymek on 28.02.15.
 */
class TournamentTermTest extends FunSuite {

  test("isValid: Simple test") {

    //given
    val enrollEndDate = new DateTime(2014, 3, 3, 0, 0, 0, 0)
    val begin = new DateTime(2014, 3, 20, 8, 0, 0, 0)
    val end = new DateTime(2014, 3, 20, 16, 0, 0, 0)
    val extraBegin = new DateTime(2014, 3, 27, 8, 0, 0, 0)
    val extraEnd = new DateTime(2014, 3, 27, 16, 0, 0, 0)
    val underTest = TournamentTerm(enrollEndDate, begin, end, extraBegin, extraEnd)


    //when
    val result = underTest.isValid

    //then
    assert(result, "isValid of correct Term should return true!")

  }

  test("isValid: EnrollmentDeadline after Tournament begin"){

    //given
    val enrollEndDate = new DateTime(2014, 3, 21, 0, 0, 0, 0)
    val begin = new DateTime(2014, 3, 20, 8, 0, 0, 0)
    val end = new DateTime(2014, 3, 20, 16, 0, 0, 0)
    val extraBegin = new DateTime(2014, 3, 27, 8, 0, 0, 0)
    val extraEnd = new DateTime(2014, 3, 27, 16, 0, 0, 0)
    val underTest = TournamentTerm(enrollEndDate, begin, end, extraBegin, extraEnd)


    //when
    val result = underTest.isValid

    //then
    assert(!result, "Enrollment deadline is after tournament start, should return false!")

  }

  test("isValid: Tournament begin is after Tournament end"){

    //given
    val enrollEndDate = new DateTime(2014, 3, 3, 0, 0, 0, 0)
    val begin = new DateTime(2014, 3, 20, 19, 0, 0, 0)
    val end = new DateTime(2014, 3, 20, 16, 0, 0, 0)
    val extraBegin = new DateTime(2014, 3, 27, 8, 0, 0, 0)
    val extraEnd = new DateTime(2014, 3, 27, 16, 0, 0, 0)
    val underTest = TournamentTerm(enrollEndDate, begin, end, extraBegin, extraEnd)


    //when
    val result = underTest.isValid

    //then
    assert(!result, "Tournament begin is after Tournament end, should return false!")

  }

  test("isValid: Tournament extraBegin is after Tournament extraEnd"){

    //given
    val enrollEndDate = new DateTime(2014, 3, 3, 0, 0, 0, 0)
    val begin = new DateTime(2014, 3, 20, 8, 0, 0, 0)
    val end = new DateTime(2014, 3, 20, 16, 0, 0, 0)
    val extraBegin = new DateTime(2014, 3, 28, 8, 0, 0, 0)
    val extraEnd = new DateTime(2014, 3, 27, 16, 0, 0, 0)
    val underTest = TournamentTerm(enrollEndDate, begin, end, extraBegin, extraEnd)


    //when
    val result = underTest.isValid

    //then
    assert(!result, "Tournament extraBegin is after Tournament extraEnd, should return false!")

  }

  test("isValid: Tournament begin is after tournament extraBegin"){

    //given
    val enrollEndDate = new DateTime(2014, 3, 3, 0, 0, 0, 0)
    val begin = new DateTime(2014, 3, 27, 9, 0, 0, 0)
    val end = new DateTime(2014, 3, 20, 16, 0, 0, 0)
    val extraBegin = new DateTime(2014, 3, 27, 8, 0, 0, 0)
    val extraEnd = new DateTime(2014, 3, 27, 16, 0, 0, 0)
    val underTest = TournamentTerm(enrollEndDate, begin, end, extraBegin, extraEnd)


    //when
    val result = underTest.isValid

    //then
    assert(!result, "Tournament begin is after tournament extraBegin, should return false!")

  }

}

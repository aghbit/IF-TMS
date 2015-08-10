package models.tournament.tournamentfields

import org.scalatest.FunSuite

/**
 * Created by szymek on 28.02.15.
 */
class TournamentSettingsTest extends FunSuite {

  test("areValid: simple test") {

    //given
    val underTestValid = TournamentSettings(1, 2, canEnroll = true, 1)
    val underTestNotValid1 = TournamentSettings(-1, 2, canEnroll = true, 1)
    val underTestNotValid2 = TournamentSettings(0, 2, canEnroll = true, 1)
    val underTestNotValid3 = TournamentSettings(1, 1, canEnroll = true, 1)
    val underTestNotValid4 = TournamentSettings(1, -1, canEnroll = true, 1)

    //when
    val resultValid = underTestValid.isValid
    val resultNotValid1 = underTestNotValid1.isValid
    val resultNotValid2 = underTestNotValid2.isValid
    val resultNotValid3 = underTestNotValid3.isValid
    val resultNotValid4 = underTestNotValid4.isValid

    //then
    assert(resultValid, "Should return true")
    assert(!resultNotValid1, "Negative number of Pitches: Should return false")
    assert(!resultNotValid2, "0 Pitches: Should return false")
    assert(!resultNotValid3, "1 Team: Should return false")
    assert(!resultNotValid4, "Negative number of Teams: Should return false")
  }

}

package ITE.tournaments

import models.statistics.Statistics
import models.strategy.TournamentStrategy
import models.team.Team
import models.tournament.tournaments.TournamentDiscipline.BeachVolleyball
import models.tournament.tournaments._
import models.tournament.tournaments.TournamentSettings
import models.user.userproperties.UserProperties
import models.user.users.userimpl.UserImpl
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable.ListBuffer

/**
 * Created by Przemek
 */
@RunWith(classOf[JUnitRunner])
class BreakTest extends FunSuite with MockitoSugar with BeforeAndAfter {
  var instance: Tournament = _
  var properties: TournamentProperties = _
  var teams: ListBuffer[Team] = _
  var discipline: TournamentDiscipline = _
  var stat: Option[Statistics] = _
  var strategy: TournamentStrategy = _

  before {
    /* ----- Description ----- */
    val name = "Test instance of class Tournament"
    val place = "Random"
    val desc = "Properly written sentence in English"
    val description = new TournamentDescription(name, place, desc)

    /* ----- Term ----- */
    val enroll_deadline = DateTime.parse("2014-12-30 17:34:42Z")
    val enroll_begin = DateTime.parse("2014-12-26 17:34:42Z")
    val enroll_end = DateTime.parse("2014-12-28 17:34:42Z")
    val enroll_ex_begin = DateTime.parse("2014-12-28 17:35:42Z")
    val enroll_ex_end = DateTime.parse("2014-12-30 17:33:42Z")
    val term = new TournamentTerm(enroll_deadline, enroll_begin, enroll_end, enroll_ex_begin, enroll_ex_end)

    /* ---- Settings ----- */
    val NoP = 4
    val NoT = 0
    val level = TournamentLevel.Pro
    val canEnroll = false
    val settings = new TournamentSettings(NoP, NoT, canEnroll, level)

    /* ---- Strategy (We need to talk) ---- */
    strategy = mock[TournamentStrategy] // !!! CHANGE IT LATER !!!

    /* ---- Staff ---- */
    // admin
    val name1 = "John"
    val login = "Brown"
    val passwd = "123456a"
    val phone = "123456789"
    val mail = "hotJohn666@oxygen.com"
    stat = mock[Option[Statistics]] // !!! CHANGE IT LATER !!!

    val admin = UserImpl.apply(new UserProperties(name1, login, passwd, phone, mail), stat)
    val RefereeList = ListBuffer(admin, admin, admin)
    val staff = new TournamentStaff(admin, RefereeList)

    properties = new TournamentProperties(description, term, settings, strategy, staff)
    instance = BeforeEnrollment.apply(properties, BeachVolleyball)
  }

  /*
  test("") {

    //given

    //when

    //then

  }
 */
}

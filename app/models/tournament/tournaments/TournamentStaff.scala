package models.tournament.tournaments

import models.user.User
import scala.collection.mutable.ListBuffer

/**
 * Created by Szymek.
 */
class TournamentStaff(val admin:User,
                      var Referees: ListBuffer[User]) {

}

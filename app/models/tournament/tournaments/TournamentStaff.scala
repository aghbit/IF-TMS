package models.tournament.tournaments

import models.user.User

/**
 * Created by Szymek.
 */
class TournamentStaff(val admin: User,
                      val Referees: Option[List[User]]) {

  def addReferee(user: User) = ???

}

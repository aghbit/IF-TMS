package models.tournament.tournaments

import models.user.User
import models.tournament.tournaments.TournamentProperties

/**
 * Created by Szymek.
 */
class TournamentStaff(val admin:User,
                      var Referees:Option[List[User]]) {

  def addReferee(user:User) = {
    //var newProps: TournamentProperties = new TournamentProperties()
  }
}

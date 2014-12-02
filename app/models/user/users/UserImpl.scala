package models.user.users

import models.statistics.Statistics
import models.team.Team
import models.tournament.tournaments.Tournament
import models.user.User
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class UserImpl(val _id:Option[BSONObjectID],
                val personalData: UserProperties,
                val statistics: Statistics) extends User
{

}

package models.users

import models.statistics.Statistics
import models.teams.Team
import models.tournament.Tournament
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class UserImpl(val _id:Option[BSONObjectID],
                val personalData: UserProperties,
                val tournaments: List[Tournament],
                val teams: List[Team],
                val statistics: Statistics) extends User
{

}

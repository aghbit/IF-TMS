package repositories

import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.user.User
import models.user.users.userimpl.UserImpl
import org.springframework.data.mongodb.core.query.Query

/**
 * Created by Szymek.
 */
class TeamRepository extends Repository[Team] {

  override val collectionName: String = "Teams"
  override val clazz: Class[Team] = classOf[Team]

  def insert(team: Team) = {
    if (!team.isReadyToSave){
      throw new IllegalArgumentException("Team is not ready to save!")
    }else{
      mongoTemplate.save(team, collectionName)
    }
  }

}

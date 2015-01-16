package repositories

import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import models.user.users.userimpl.UserImpl
import org.springframework.data.mongodb.core.query.Query

/**
 * Created by Szymek.
 */
class TeamRepository extends Repository{

  val collectionName:String = "Teams"

  def insert(team: Team) = {
    mongoTemplate.save(team, collectionName)
  }

  def find(query: Query, clazz: Class[VolleyballTeam]) = {
    mongoTemplate.find(query, clazz, collectionName)
  }

  def remove(query: Query, clazz: Class[VolleyballTeam]) = {
    mongoTemplate.findAllAndRemove(query, clazz, collectionName)
  }

}

package repositories

import models.team.Team
import models.team.teams.volleyball.volleyballs.VolleyballTeam
import org.springframework.data.mongodb.core.query.Query

/**
 * Created by Szymek.
 */
class TestModelRepository extends Repository{
  val collectionName:String = "Test"

  def insert(team: TestModel) = {
    mongoTemplate.save(team, collectionName)
  }

  def find(query: Query, clazz: Class[TestModel]) = {
    mongoTemplate.find(query, clazz, collectionName)
  }

  def remove(query: Query, clazz: Class[TestModel]) = {
    mongoTemplate.findAllAndRemove(query, clazz, collectionName)
  }
}

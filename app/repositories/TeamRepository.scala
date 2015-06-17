package repositories

import com.mongodb.{BasicDBObject, DBObject}
import configuration.CasbahMongoDBConfiguration
import models.team.Team
import repositories.converters.TeamDBObjectConverter

/**
 * Created by Szymek.
 */
class TeamRepository {

  val collectionName: String = "Teams"



  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)
  @throws[IllegalArgumentException]
  def insert(team: Team) = {
    if (!team.isReadyToSave){
      throw new IllegalArgumentException("Team is not ready to save!")
    }else{
      collection.save(TeamDBObjectConverter.toDbObject(team))
    }
  }

  def findOne(criteria:DBObject):Option[Team] = {
    collection.findOne(criteria) match {
      case Some(obj) => {
        Some(TeamDBObjectConverter.fromDBObject(obj))
      }
      case None => None
    }
  }

  def remove(team: Team) = collection.remove(new BasicDBObject("_id", team._id))

  def dropCollection() = collection.dropCollection()

}

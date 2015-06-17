package repositories

import com.mongodb.{BasicDBObject, MongoException, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, Imports, MongoDBObjectBuilder}
import configuration.CasbahMongoDBConfiguration
import models.player.players.Captain
import models.team.Team
import models.team.teams.volleyball.volleyballs.{VolleyballTeam, BeachVolleyballTeam}
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Query
import repositories.converters.{TeamDBObjectConverter, PlayerDBObjectConverter}

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

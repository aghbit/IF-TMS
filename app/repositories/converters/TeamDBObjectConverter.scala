package repositories.converters

import com.mongodb.{MongoException, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, Imports, MongoDBObjectBuilder}
import models.player.players.Captain
import models.team.Team
import models.team.teams.volleyball.volleyballs.{BeachVolleyballTeam, VolleyballTeam}
import org.bson.types.ObjectId

/**
 * Created by Szymek Seget on 17.06.15.
 */
object TeamDBObjectConverter {


  def toDbObject(team:Team):DBObject = {
    val builder = new MongoDBObjectBuilder
    builder += ("_id" -> team._id)
    builder += ("_class" -> team.getClass.getName)
    builder += ("name" -> team.name)
    builder += ("playersNumber" -> team.getPlayersNumber)
    builder += ("benchWarmersNumber" -> team.getBenchWarmersNumber)
    builder += ("captain" -> PlayerDBObjectConverter.toDbObject(team.getCaptain))
    val playersDbObjects = team.getPlayersAsList.map(player => PlayerDBObjectConverter.toDbObject(player))
    builder += ("players" -> playersDbObjects)
    val benchWarmersDbObjects = team.getBenchWarmersAsList.map(
      benchWarmer => PlayerDBObjectConverter.toDbObject(benchWarmer)
    )
    builder += ("benchWarmers"-> benchWarmersDbObjects)
    builder.result()
  }
  def fromDBObject(obj: DBObject): Team = {
    val document = Imports.wrapDBObj(obj.asInstanceOf[DBObject])
    val id = document.getAsOrElse[ObjectId]("_id", throw new MongoException("_id not found!"))
    val _class = document.getAsOrElse[String]("_class", throw new MongoException("_class not found!"))
    val name = document.getAsOrElse[String]("name", throw new MongoException("name not found!"))
    val playersNumber = document.getAsOrElse[Int]("playersNumber",
      throw new MongoException("playersNumber not found"))
    val benchWarmersNumber = document.getAsOrElse[Int]("benchWarmersNumber",
      throw new MongoException("benchWarmersNumber not found"))
    val captainDbObject = document.getAsOrElse[DBObject]("captain", throw new MongoException("captain not found"))
    val captain = PlayerDBObjectConverter.fromDbObject(captainDbObject).asInstanceOf[Captain]
    val playersDbObjects = document.getAsOrElse[MongoDBList]("players", throw new MongoException("players not found"))
    val benchWarmersDbObjects = document.getAsOrElse[MongoDBList]("benchWarmers",
      throw new MongoException("benchWarmers not found!"))
    val players = playersDbObjects.map(obj => PlayerDBObjectConverter.fromDbObject(obj.asInstanceOf[DBObject]))
    val benchWarmers = benchWarmersDbObjects.map(obj => PlayerDBObjectConverter.fromDbObject(obj.asInstanceOf[DBObject]))
    _class match {
      case "models.team.teams.volleyball.volleyballs.VolleyballTeam" =>
        val team:Team = new VolleyballTeam(id, name, playersNumber, benchWarmersNumber)
        players.foreach(p=>team.addPlayer(p))
        benchWarmers.foreach(b => team.addBenchWarmer(b))
        team.setCaptain(captain)
        team
      case "models.team.teams.volleyball.volleyballs.BeachVolleyballTeam" =>
        val team:Team = new BeachVolleyballTeam(id, name, playersNumber, benchWarmersNumber)
        players.foreach(p => team.addPlayer(p))
        benchWarmers.foreach(b => team.addBenchWarmer(b))
        team.setCaptain(captain)
        team
      case _ => throw new Exception("Wrong class name!")
    }
  }

}

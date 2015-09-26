package repositories

import com.mongodb.casbah.Imports
import com.mongodb.{MongoException, BasicDBObject, DBObject}
import com.mongodb.BasicDBObject
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObjectBuilder}
import com.mongodb.util.JSON
import configuration.CasbahMongoDBConfiguration
import models.strategy.Match
import models.strategy.strategies.RoundRobinStrategy
import models.strategy.structures.EliminationTable
import models.tournament.tournamenttype.TournamentType
import models.tournament.tournamenttype.tournamenttypes.{Speedminton, Volleyball, BeachVolleyball}
import org.bson.types.ObjectId
import repositories.converters.MatchFromDBObjectConverter
import repositories.factories.ReflectionFactory

/**
 * Created by Szymek Seget on 08.09.15.
 */
class EliminationTableRepository {

  val collectionName: String = "EliminationTables"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  def remove(query: BasicDBObject) = {
    collection.remove(query)
  }

  def contains(query: BasicDBObject): Boolean = {
    findOne(query) match {
      case Some(i) => true
      case None => false
    }
  }

  def insert(obj:EliminationTable):Unit = {
    val builder = new MongoDBObjectBuilder()
    builder += ("_id" -> obj._id)
    builder += ("teamsNumber" -> obj.teamsNumber)
    val className = obj.tournamentType.getClass.getName
    builder += ("discipline" -> className)
    val clazz = obj.getClass.getName
    builder += ("_class" -> clazz)
    var matches:List[Match] = List()
    obj.mapMatches(m => {
      matches = matches ::: List(m)
      m
    })
    builder += ("matches" -> matches.map(m => JSON.parse(m.toJson.toString()).asInstanceOf[DBObject]))
    collection.insert(builder.result())
  }

  def findOne(query: DBObject):Option[EliminationTable] = {
    collection.findOne(query) match {
      case Some(dbObj) =>
        val document = Imports.wrapDBObj(dbObj.asInstanceOf[DBObject])
        val eliminationTreeID = document.getAsOrElse[ObjectId]("_id", throw new MongoException("_id not found!"))
        val clazz = document.getAsOrElse[String]("_class", throw new MongoException("_class not found!"))
        val className = document.getAsOrElse[String]("discipline", throw new MongoException("discipline not found!"))
        val teamsNumber = document.getAsOrElse[Int]("teamsNumber", throw new MongoException("teamsNumber not found!"))
        val matchesDBObject = document.getAsOrElse[MongoDBList]("matches", throw new MongoException("matches not found!"))
        val strategy = clazz match {
          case "models.strategy.structures.eliminationtables.RoundRobinTable" => RoundRobinStrategy
          case _ => throw new Exception("NOT IMPLEMENTED!")
        }
        val discipline = ReflectionFactory.build[TournamentType](className) match {
          case Some(s) => s
          case None => throw new Exception("NOT IMPLEMENTED!")
        }
        val iterator = matchesDBObject.iterator
        var matches:List[Match] = List()
        while(iterator.hasNext) {
          val doc = iterator.next().asInstanceOf[DBObject]
          val m = MatchFromDBObjectConverter.matchFromDBObject(doc, discipline)
          matches =  matches ::: List(m)
        }
        val eliminationTable = strategy.initEmpty(eliminationTreeID, teamsNumber, discipline)
        var i = 0
        eliminationTable match {
          case t:EliminationTable => {
            t.foreachNode(
              node => {node.value = Some(matches(i))
                i=i+1
              }
            )
            Some(t)
          }
          case _ => throw new Exception("NOT IMPLEMENTED!")
        }
      case None => None
    }
  }
}

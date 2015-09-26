package repositories

import com.mongodb.{MongoException, BasicDBObject, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, Imports, MongoDBObjectBuilder, MongoDBObject}
import com.mongodb.util.JSON
import configuration.CasbahMongoDBConfiguration
import models.enums.ListEnum
import models.strategy.scores.BeachVolleyballScore
import models.strategy.strategies.{SingleEliminationStrategy, DoubleEliminationStrategy}
import models.strategy.structures.EliminationTree
import models.strategy.{Score, Match}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import models.tournament.tournamenttype.tournamenttypes.{Volleyball, BeachVolleyball}
import org.bson.types.ObjectId
import repositories.converters.MatchFromDBObjectConverter
import repositories.factories.ReflectionFactory

/**
 * Created by Szymek Seget on 28.05.15.
 */
class EliminationTreeRepository {


  val collectionName: String = "EliminationTrees"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  private val teamsRepository = new TeamRepository()


  def remove(query: BasicDBObject) = {
    collection.remove(query)
  }


  def contains(query: BasicDBObject): Boolean = {
    findOne(query) match {
      case Some(i) => true
      case None => false
    }
  }

  def insert(obj: EliminationTree): Unit = {
    val builder = new MongoDBObjectBuilder()
    builder += ("_id" ->obj._id)
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

  def findOne(criteria: DBObject):Option[EliminationTree] = {
    collection.findOne(criteria) match {
      case Some(dbObj) => {
        val document = Imports.wrapDBObj(dbObj.asInstanceOf[DBObject])
        val eliminationTreeID = document.getAs[ObjectId]("_id").get
        val clazz = document.getAsOrElse[String]("_class", throw new MongoException("_class not found!"))
        val className = document.getAsOrElse[String]("discipline", throw new MongoException("discipline not found!"))
        val teamsNumber = document.getAs[Int]("teamsNumber").get
        val matchesDBObjects = document.getAs[MongoDBList]("matches").get
        val iterator = matchesDBObjects.iterator
        val strategy = clazz match {
          case "models.strategy.structures.eliminationtrees.SingleEliminationTree" => SingleEliminationStrategy
          case "models.strategy.structures.eliminationtrees.DoubleEliminationTree" => DoubleEliminationStrategy
          case _ => throw new NoSuchElementException("This strategy is not implemented")
        }
        val discipline = ReflectionFactory.build[TournamentType](className) match {
          case Some(s) => s
          case None => throw new Exception("NOT IMPLEMENTED!")
        }
        var matches:List[Match] = List()
        while(iterator.hasNext) {
          val doc = iterator.next().asInstanceOf[DBObject]
          val m = MatchFromDBObjectConverter.matchFromDBObject(doc, discipline)
          matches =  matches ::: List(m)
        }
        matches = matches.sortWith((m1,m2) => m1.id < m2.id)
        val eliminationTree = strategy.initEmpty(eliminationTreeID, teamsNumber, discipline)
        var i=0
        eliminationTree match {
          case t:EliminationTree =>{
            t.foreachNode(node => {
              node.value = matches(i)
              i=i+1
            })
            Some(t)
          }
          case _ => ???
        }
        }


      case None => None
    }

  }

  def dropCollection() = {
    collection.dropCollection()
  }
}

package repositories

import com.mongodb.casbah.commons.{Imports, MongoDBList, MongoDBObjectBuilder}
import com.mongodb.{BasicDBObject, DBObject, MongoException}
import configuration.CasbahMongoDBConfiguration
import models.strategy.structures.EliminationTree
import models.strategy.{EliminationStrategy, Match}
import models.tournament.tournamenttype.TournamentType
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
    builder += ("strategy" -> obj.strategy.getClass.getName)
    var matches:List[Match] = List()
    obj.mapMatches(m => {
      matches = matches ::: List(m)
      m
    })
    builder += ("matches" -> matches.map(m => MatchFromDBObjectConverter.toDbObject(m)))
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
        val strategyClassName = document.getAsOrElse[String]("strategy", throw new MongoException("_class not found!"))

        val strategy = ReflectionFactory.build[EliminationStrategy](strategyClassName) match {
          case Some(s) => s
          case None => throw new Exception("This strategy is not implemented")
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

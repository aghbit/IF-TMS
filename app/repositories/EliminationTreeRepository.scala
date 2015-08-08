package repositories

import com.mongodb.{MongoException, BasicDBObject, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, Imports, MongoDBObjectBuilder, MongoDBObject}
import com.mongodb.util.JSON
import configuration.CasbahMongoDBConfiguration
import models.enums.ListEnum
import models.strategy.scores.BeachVolleyballScore
import models.strategy.strategies.{SingleEliminationStrategy, DoubleEliminationStrategy}
import models.strategy.{EliminationTree, Match}
import models.team.Team
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.bson.types.ObjectId

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
  //  val clazz = obj.tournamentType.getClass
  //  builder += ("type" -> clazz.toString)
    val clazz = obj.getClass.getName()
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
        val tournamentType = BeachVolleyball
        val document = Imports.wrapDBObj(dbObj.asInstanceOf[DBObject])
        val eliminationTreeID = document.getAs[ObjectId]("_id").get
        val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not found!"))
        val teamsNumber = document.getAs[Int]("teamsNumber").get
        val matchesDBObjects = document.getAs[MongoDBList]("matches").get
        val iterator = matchesDBObjects.iterator
        var matches:List[Match] = List()
        while(iterator.hasNext) {
          val doc = iterator.next().asInstanceOf[DBObject]
          val m = matchFromDBObject(doc)
          matches =  matches ::: List(m)
        }
        matches = matches.sortWith((m1,m2) => m1.id < m2.id)
        val strategy = className match {
          case "models.strategy.eliminationtrees.SingleEliminationTree" => SingleEliminationStrategy
          case "models.strategy.eliminationtrees.DoubleEliminationTree" => DoubleEliminationStrategy
          case _ => throw new NoSuchElementException("This strategy is not implemented")
        }
        val eliminationTree = strategy.initEmptyTree(eliminationTreeID, teamsNumber, tournamentType)
        var i=0
        eliminationTree.foreachTreeNodes(node => {
          node.value = matches(i)
          i=i+1
        })
        Some(eliminationTree)
      }

      case None => None
    }

  }

  private def matchFromDBObject(dBObject: DBObject):Match = {
    val document:MongoDBObject = Imports.wrapDBObj(dBObject.asInstanceOf[DBObject])
    val matchID = document.getAs[Int]("_id").get

    val hostDBObject: MongoDBObject = document.getAs[MongoDBObject]("host").orNull
    val guestDBObject: MongoDBObject = document.getAs[MongoDBObject]("guest").orNull
    var host:Option[Team] = None
    if(hostDBObject != null){
      val hostDBID = hostDBObject.getAs[String]("_id").get
      val hostID = new ObjectId(hostDBID)
      val hostQuery = new BasicDBObject("_id", hostID)
      val hosts = teamsRepository.findOne(hostQuery)
      if(hosts.isDefined){
        host = Some(hosts.get)
      }
    }
    var guest:Option[Team] = None
    if(guestDBObject != null){
      val guestDBID = guestDBObject.getAs[String]("_id").get
      val guestID = new ObjectId(guestDBID)
      val guestQuery = new BasicDBObject("_id", guestID)
      val guests = teamsRepository.findOne(guestQuery)
      if(guests.isDefined){
        guest = Some(guests.get)
      }
    }

    val setsObjectList = document.getAs[MongoDBList]("sets").get

    val score:BeachVolleyballScore = BeachVolleyballScore()

    val iterator = setsObjectList.iterator
    var i:Int=1

    while(iterator.hasNext){
      val set = Imports.wrapDBObj(iterator.next().asInstanceOf[DBObject]).getAs[MongoDBObject](i.toString).get
      val hostScore:Int = set.getAs[Int]("host").get
      val guestScore:Int = set.getAs[Int]("guest").get
      score.addSet()
      score.setScoreInLastSet(hostScore, guestScore)
      i=i+1
    }

    val m = new Match(matchID, host, guest, score)
    m
  }

  def dropCollection() = {
    collection.dropCollection()
  }
}

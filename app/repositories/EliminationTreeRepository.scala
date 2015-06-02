package repositories

import com.mongodb.DBObject
import com.mongodb.casbah.commons.{MongoDBList, Imports, MongoDBObjectBuilder, MongoDBObject}
import com.mongodb.util.JSON
import configuration.CasbahMongoDBConfiguration
import models.enums.ListEnum
import models.strategy.scores.BeachVolleyballScore
import models.strategy.strategies.DoubleEliminationStrategy
import models.strategy.{EliminationTree, Match}
import models.team.Team
import models.tournament.tournamenttype.tournamenttypes.BeachVolleyball
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.{Criteria, Query}

/**
 * Created by Szymek Seget on 28.05.15.
 */
class EliminationTreeRepository {
  val collectionName: String = "EliminationTrees"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)

  private val teamsRepository = new TeamRepository()

  def insert(obj: EliminationTree): Unit = {
    val builder = new MongoDBObjectBuilder()
    builder += ("_id" ->obj._id)
    builder += ("teamsNumber" -> obj.teamsNumber)
  //  val clazz = obj.tournamentType.getClass
  //  builder += ("type" -> clazz.toString)
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
        val eliminationTreeID = (document.getAs[ObjectId]("_id").get)
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
        val eliminationTree = DoubleEliminationStrategy.initEmptyTree(eliminationTreeID, teamsNumber, tournamentType)
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
      val hostQuery = new Query(Criteria where "_id" is hostID)
      val hosts = teamsRepository.find(hostQuery)
      if(!hosts.isEmpty){
        host = Some(hosts.get(ListEnum.head))
      }
    }
    var guest:Option[Team] = None
    if(guestDBObject != null){
      val guestDBID = guestDBObject.getAs[String]("_id").get
      val guestID = new ObjectId(guestDBID)
      val guestQuery = new Query(Criteria where "_id" is guestID)
      val guests = teamsRepository.find(guestQuery)
      if(!guests.isEmpty){
        guest = Some(guests.get(ListEnum.head))
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

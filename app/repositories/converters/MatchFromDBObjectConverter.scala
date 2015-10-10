package repositories.converters

import com.mongodb.casbah.commons.{Imports, MongoDBList, MongoDBObject, MongoDBObjectBuilder}
import com.mongodb.{BasicDBObject, DBObject, MongoException}
import models.Participant
import models.strategy.scores.newscores.beachvolleyball.BeachVolleyballSet
import models.strategy.{Match, Score}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import repositories.{PlayerRepository, TeamRepository}

/**
 * Created by Szymek Seget on 08.09.15.
 */
object MatchFromDBObjectConverter {

  private val teamsRepository = new TeamRepository

  private val playersRepository = new PlayerRepository


  def toDbObject(m: Match): DBObject = {
    val builder = new MongoDBObjectBuilder
    builder += ("_id" -> m.id,
      m.host match {
        case Some(h) => "host" -> new BasicDBObject("_id",h._id.toString).append("name",h.getNickName)
        case None => "host" -> null
      },
      m.guest match {
        case Some(h) => "guest" -> new BasicDBObject("_id",h._id.toString).append("name",h.getNickName)
        case None => "guest" -> null
      }
    )
    m.score.addPointsContainer(new BeachVolleyballSet(10,3))
    builder += "pointsContainers" ->
      m.score.pointsContainers.map(p => PointsContainerDBObjectConverter.toDBObject(p))
    builder.result()
  }

  def matchFromDBObject(dBObject: DBObject, discipline:TournamentType):Match = {
    val document:MongoDBObject = Imports.wrapDBObj(dBObject.asInstanceOf[DBObject])
    val matchID = document.getAs[Int]("_id").get

    val hostDBObject: MongoDBObject = document.getAs[MongoDBObject]("host").orNull
    val guestDBObject: MongoDBObject = document.getAs[MongoDBObject]("guest").orNull
    var host:Option[Participant] = None
    if(hostDBObject != null){
      val hostDBID = hostDBObject.getAs[String]("_id").get
      val hostID = new ObjectId(hostDBID)
      val hostQuery = new BasicDBObject("_id", hostID)
      host = teamsRepository.findOne(hostQuery)
      if(host.isEmpty){
        host = playersRepository.findOne(hostQuery)
      }
    }
    var guest:Option[Participant] = None
    if(guestDBObject != null){
      val guestDBID = guestDBObject.getAs[String]("_id").get
      val guestID = new ObjectId(guestDBID)
      val guestQuery = new BasicDBObject("_id", guestID)
      guest = teamsRepository.findOne(guestQuery)
      if(guest.isEmpty) {
        guest = playersRepository.findOne(guestQuery)
      }
    }

    val pointsContainersObjectList = document.getAsOrElse[MongoDBList]("pointsContainers", throw new MongoException("pointsContainer not found!"))

    val score:Score = discipline.getNewScore()

    pointsContainersObjectList.foreach(obj => {
      score.addPointsContainer(PointsContainerDBObjectConverter.fromDBObject(obj.asInstanceOf[DBObject]))
    })

    val m = new Match(matchID, host, guest, score)
    m
  }

}

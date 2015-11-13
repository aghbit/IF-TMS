package repositories.converters

import java.util

import com.mongodb.casbah.commons.{Imports, MongoDBList, MongoDBObject, MongoDBObjectBuilder}
import com.mongodb.{DBObject, MongoException}
import models.Participant
import models.player.Player
import models.strategy.{EliminationStrategy, ParticipantType}
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields._
import models.tournament.tournamentstates._
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import org.joda.time.DateTime
import repositories.factories.ReflectionFactory

import scala.collection.JavaConversions._

/**
 * Created by Szymek Seget on 17.06.15.
 */
object TournamentDBObjectConverter {


  def fromDbObject(obj:DBObject) = {
    val document = Imports.wrapDBObj(obj.asInstanceOf[DBObject])
    val id = document.getAsOrElse[ObjectId]("_id", throw new MongoException("_id not found!"))
    val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not found!"))
    val properties = propertiesFromDbObject(document.getAsOrElse[DBObject]("properties",
    throw new MongoException("properties not found")))
    val participantTypeString = document.getAsOrElse[String]("participantType", throw new MongoException("participantType not found!"))
    val participantType = ParticipantType.withName(participantTypeString)
    val participantsDBObjects = document.getAsOrElse[MongoDBList]("participants", throw new MongoException("participants not found!"))
    val participants = participantsDBObjects.map(o =>
      participantType match {
        case ParticipantType.TEAM => TeamDBObjectConverter.fromDBObject(o.asInstanceOf[DBObject])
        case ParticipantType.PLAYER => PlayerDBObjectConverter.fromDbObject(o.asInstanceOf[DBObject])
      })
    val staff = staffFromDbObject(document.getAsOrElse[DBObject]("staff",
    throw new MongoException("staff not found"))
    )
    val strategy = strategyFromDbObject(document.getAsOrElse[DBObject]("strategy",
    throw new MongoException("strategy not found"))
    )
    val discipline = disciplineFromDbObject(document.getAsOrElse[DBObject]("discipline",
    throw new MongoException("discipline not found"))
    )

    className match {
      case "models.tournament.tournamentstates.BeforeEnrollment" =>
        Some(new BeforeEnrollment(id, properties, staff, strategy, discipline))
      case "models.tournament.tournamentstates.Enrollment" =>
        Some(new Enrollment(id, properties, new util.ArrayList(participants), staff, strategy, discipline))
      case "models.tournament.tournamentstates.Break" =>
        Some(new Break(id, properties, new util.ArrayList[Participant](participants), staff, strategy, discipline))
      case "models.tournament.tournamentstates.DuringTournament" =>
        Some(new DuringTournament(id, properties, new util.ArrayList[Participant](participants), staff, strategy, discipline))
      case "models.tournament.tournamentstates.AfterTournament" =>
        Some(new AfterTournament(id, properties, new util.ArrayList[Participant](participants), staff, strategy, discipline))
      case _ => None
    }
  }


  def toDbObject(tournament:Tournament) = {
    val builder = new MongoDBObjectBuilder
    builder += ("_id" -> tournament._id)
    builder += ("_class" -> tournament.getClass.getName)
    builder += ("properties" -> propertiesToDbObject(tournament.properties))
    builder += ("participants" -> tournament.getParticipants.map {
      case t: Team => TeamDBObjectConverter.toDbObject(t)
      case p: Player => PlayerDBObjectConverter.toDbObject(p)
    })
    builder += ("staff" -> staffToDbObject(tournament.staff))
    builder += ("strategy" -> strategyDbObject(tournament.strategy))
    builder += ("discipline" -> disciplineToDbObject(tournament.discipline))
    builder += ("participantType" -> tournament.discipline.getParticipantType.toString)
    builder.result()
  }

  private def disciplineToDbObject(discipline: TournamentType) = {
    val builder = new MongoDBObjectBuilder
    builder += ("_class" -> discipline.getClass.getName)
    builder.result()
  }

  private def staffToDbObject(staff: TournamentStaff) = {
    val builder = new MongoDBObjectBuilder
    builder += ("admin" -> staff.admin)
    if(staff.Referees.nonEmpty){
      throw new Exception("NOT IMPLEMENTED YET IN REPO!")
    }
    builder += ("referees" -> List())
    builder.result()
  }

  private def strategyDbObject(strategy: EliminationStrategy) = {
    val builder = new MongoDBObjectBuilder
    builder += ("_class" -> strategy.getClass.getName)
    builder.result()
  }

  private def propertiesToDbObject(properties: TournamentProperties): DBObject = {
    val descriptionBuilder = new MongoDBObjectBuilder
    descriptionBuilder += ("name" -> properties.description.name)
    descriptionBuilder += ("place" -> properties.description.place)
    descriptionBuilder += ("description" -> properties.description.description)

    val termBuilder = new MongoDBObjectBuilder
    termBuilder += ("enrollDeadline" -> properties.term.enrollDeadline)
    termBuilder += ("begin" -> properties.term.begin)
    termBuilder += ("end" -> properties.term.end)
    termBuilder += ("extraBegin" -> properties.term.extraBegin)
    termBuilder += ("extraEnd" -> properties.term.extraEnd)

    val settingsBuilder = new MongoDBObjectBuilder
    settingsBuilder += ("numberOfPitches" -> properties.settings.numberOfPitches)
    settingsBuilder += ("numberOfTeams" -> properties.settings.numberOfTeams)
    settingsBuilder += ("canEnroll" -> properties.settings.canEnroll)
    settingsBuilder += ("level" -> properties.settings.level)

    val builder = new MongoDBObjectBuilder
    builder += ("description" -> descriptionBuilder.result())
    builder += ("term" -> termBuilder.result())
    builder += ("settings" -> settingsBuilder.result())
    builder.result()

  }

  def propertiesFromDbObject(obj: DBObject) = {
    val document = Imports.wrapDBObj(obj)
    val descriptionDbObject = document.getAsOrElse[MongoDBObject]("description",
    throw new MongoException("description not found")
    )
    val termDbObject = document.getAsOrElse[MongoDBObject]("term",
    throw new MongoException("term not found")
    )
    val settingsDbObject = document.getAsOrElse[MongoDBObject]("settings",
    throw new MongoException("settings not found"))

    val name = descriptionDbObject.getAsOrElse[String]("name", throw new MongoException("name not found"))
    val place = descriptionDbObject.getAsOrElse[String]("place", throw new MongoException("place not found"))
    val description = descriptionDbObject.getAsOrElse[String]("description",
    throw new MongoException("description not found")
    )
    val enrollDeadline = termDbObject.getAsOrElse[DateTime]("enrollDeadline",
    throw new MongoException("enrollDeadline not found")
    )
    val begin = termDbObject.getAsOrElse[DateTime]("begin", throw new MongoException("begin not found"))
    val end = termDbObject.getAsOrElse[DateTime]("end", throw new MongoException("end not found"))
    val extraBegin = termDbObject.getAsOrElse[DateTime]("extraBegin",
    throw new MongoException("extraBegin not found")
    )
    val extraEnd = termDbObject.getAsOrElse[DateTime]("extraEnd",
    throw new MongoException("extraEnd not found")
    )
    val numberOfPitches = settingsDbObject.getAsOrElse[Int]("numberOfPitches",
    throw new MongoException("numberOfPitches not found")
    )
    val numberOfTeams = settingsDbObject.getAsOrElse[Int]("numberOfTeams",
    throw new MongoException("numberOfTeams not found")
    )
    val canEnroll = settingsDbObject.getAsOrElse[Boolean]("canEnroll",
    throw new MongoException("canEnroll not found")
    )
    val level = settingsDbObject.getAsOrElse[Int]("level", throw new MongoException("level not found"))

    val tournamentDescription = new TournamentDescription(name, place, description)
    val tournamentTerms = new TournamentTerm(enrollDeadline, begin, end, extraBegin, extraEnd)
    val tournamentSettings = new TournamentSettings(numberOfPitches, numberOfTeams, canEnroll, level)
    new TournamentProperties(tournamentDescription, tournamentTerms, tournamentSettings)
  }

  def staffFromDbObject(obj: DBObject) = {
    val document = Imports.wrapDBObj(obj)
    val admin = document.getAsOrElse[ObjectId]("admin", throw new MongoException("admin not found"))
    new TournamentStaff(admin, new util.ArrayList[ObjectId]())
  }

  def strategyFromDbObject(obj: DBObject) = {
    val document = Imports.wrapDBObj(obj)
    val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not found"))
    val eliminationStrategy = ReflectionFactory.build[EliminationStrategy](className) match {
      case Some(s) => s
      case None => throw new Exception("Not implemented!")
    }
    eliminationStrategy
  }
  def disciplineFromDbObject(obj: DBObject) = {
    val document = Imports.wrapDBObj(obj)
    val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not found"))
    val discipline = ReflectionFactory.build[TournamentType](className) match {
      case Some(s) => s
      case None => throw new Exception("Not implemented!")
    }
    discipline
  }
}

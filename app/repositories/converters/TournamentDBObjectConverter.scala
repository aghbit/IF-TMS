package repositories.converters

import java.util

import com.mongodb.{MongoException, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject, Imports, MongoDBObjectBuilder}
import models.strategy.EliminationStrategy
import models.strategy.strategies.{SingleEliminationStrategy, DoubleEliminationStrategy}
import models.team.Team
import models.tournament.Tournament
import models.tournament.tournamentfields._
import models.tournament.tournamentstates._
import org.bson.types.ObjectId
import org.joda.time.DateTime
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
    val teamsDbObjects = document.getAsOrElse[MongoDBList]("teams", throw new MongoException("teams not found!"))
    val teams = teamsDbObjects.map(o => TeamDBObjectConverter.fromDBObject(o.asInstanceOf[DBObject]))
    val staff = staffFromDbObject(document.getAsOrElse[DBObject]("staff",
      throw new MongoException("staff not found"))
    )
    val strategy = strategyFromDbObject(document.getAsOrElse[DBObject]("strategy",
      throw new MongoException("strategy not found"))
    )

    className match {
      case "models.tournament.tournamentstates.BeforeEnrollment" =>
        Some(new BeforeEnrollment(id, properties, staff, strategy))
      case "models.tournament.tournamentstates.Enrollment" =>
        Some(new Enrollment(id, properties, new util.ArrayList(teams), staff, strategy))
      case "models.tournament.tournamentstates.Break" =>
        Some(new Break(id, properties, new util.ArrayList[Team](teams), staff, strategy))
      case "models.tournament.tournamentstates.DuringTournament" =>
        Some(new DuringTournament(id, properties, new util.ArrayList[Team](teams), staff, strategy))
      case "models.tournament.tournamentstates.AfterTournament" =>
        Some(new AfterTournament(id, properties, new util.ArrayList[Team](teams), staff, strategy))
      case _ => None
    }
  }

  def toDbObject(tournament:Tournament) = {
    val builder = new MongoDBObjectBuilder
    builder += ("_id" -> tournament._id)
    builder += ("_class" -> tournament.getClass.getName)
    builder += ("properties" -> propertiesToDbObject(tournament.properties))
    builder += ("teams" -> tournament.getTeams.map(t => TeamDBObjectConverter.toDbObject(t)))
    builder += ("staff" -> staffToDbObject(tournament.staff))
    builder += ("strategy" -> strategyDbObject(tournament.strategy))
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
    settingsBuilder += ("discipline" -> properties.settings.discipline)

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
    val discipline = settingsDbObject.getAsOrElse[String]("discipline",
      throw new MongoException("discipline not found")
    )
    val tournamentDescription = new TournamentDescription(name, place, description)
    val tournamentTerms = new TournamentTerm(enrollDeadline, begin, end, extraBegin, extraEnd)
    val tournamentSettings = new TournamentSettings(numberOfPitches, numberOfTeams, canEnroll, level, discipline)
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
    val eliminationStrategy = className match {
      case "models.strategy.strategies.DoubleEliminationStrategy$" => DoubleEliminationStrategy
      case "models.strategy.strategies.SingleEliminationStrategy$" => SingleEliminationStrategy
      case _ => throw new Exception("NOT IMPLEMENTED!")
    }
    eliminationStrategy
  }
}

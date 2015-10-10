package repositories.converters

import com.mongodb.{MongoException, DBObject}
import com.mongodb.casbah.commons.{Imports, MongoDBObjectBuilder}
import models.strategy.scores.newscores.{PointsContainerCompanionObject, Gems, Points, PointsContainer}
import repositories.factories.ReflectionFactory

/**
 * Created by Szymek Seget on 10.10.15.
 */
object PointsContainerDBObjectConverter {

  def fromDBObject(obj: DBObject): PointsContainer = {
    val document =Imports.wrapDBObj(obj.asInstanceOf[DBObject])
    val className = document.getAsOrElse[String]("_class", throw new MongoException("_class not found"))
    val pointsContainer = ReflectionFactory.build[PointsContainerCompanionObject](className).get
    pointsContainer.build() match {
      case p:Points => {
        p.guestPoints = document.getAsOrElse[Int]("guest", throw new MongoException("guest not found"))
        p.hostPoints = document.getAsOrElse[Int]("host", throw new MongoException("host not found"))
        p
      }
      case p:Gems => throw new Exception("_class NOT IMPLEMENTED " + className)
      case _ => throw new Exception("_class NOT IMPLEMENTED " + className)
    }
  }

  def toDBObject(p: PointsContainer): DBObject = {
    val builder = new MongoDBObjectBuilder
    builder += ("_class" -> p.getClass.getName)
    p match {
      case points:Points => {
        builder += ("host" -> points.hostPoints)
        builder += ("guest" -> points.guestPoints)
      }
      case _ => throw new Exception("Points Container not IMPLEMENTED!")
    }
    builder.result()
  }


}

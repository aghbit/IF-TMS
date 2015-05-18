package configuration

import com.mongodb.{MongoClientURI, MongoClient, Mongo}
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

/**
 * Created by Szymek.
 */

class MongoDBConfiguration(var databaseName: String) extends AbstractMongoConfiguration {
  override def getDatabaseName: String = databaseName

  override def mongo(): Mongo = {
    val URI = System.getenv("MONGO_URI")
    if(URI == null) {
      new MongoClient("127.0.0.1", 27017)
    } else {
      databaseName = "tms_db"
      new MongoClient(new MongoClientURI("mongodb://tms:tms123@ds061928.mongolab.com:61928/tms_db"))
    }
  }
}

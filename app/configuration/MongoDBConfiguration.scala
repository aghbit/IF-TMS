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
      databaseName = "tms-production"
      new MongoClient(new MongoClientURI("mongodb://tms:RjNuZ1BlNkpWeUttWEs3V0RmSm1HWHJoaGlVeHZpeER5WVJaYjlIQjBPTT0K@172.17.0.113:27017/tms-production"))
    }
  }
}

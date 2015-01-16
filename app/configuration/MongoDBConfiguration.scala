package configuration

import com.mongodb.{MongoClient, Mongo}
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

/**
 * Created by Szymek.
 */

class MongoDBConfiguration(val databaseName:String) extends AbstractMongoConfiguration {

  override def getDatabaseName: String = databaseName

  override def mongo(): Mongo = new MongoClient("127.0.0.1", 27017)
}

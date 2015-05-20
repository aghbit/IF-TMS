package configuration

import com.mongodb.{MongoClient, Mongo}
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

/**
 * Created by Szymek.
 */

class MongoDBConfiguration(val databaseName: String) extends AbstractMongoConfiguration {

  /**
   * Only for Spring Data. Don't use it. For more information check: TMS-76
   */
  def this() = this(null)

  override def getDatabaseName: String = databaseName

  override def mongo(): Mongo = new MongoClient("127.0.0.1", 27017)
}

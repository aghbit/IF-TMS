package configuration

import org.springframework.data.mongodb.core.MongoTemplate

/**
 * Created by Szymek.
 */
object WebAppConfiguration {

  val databaseName: String = "TMS_DB"

  def mongoTemplate: MongoTemplate = {
    val mongoDBConfiguration: MongoDBConfiguration = new MongoDBConfiguration(databaseName)
    new MongoTemplate(mongoDBConfiguration.mongo(), mongoDBConfiguration.databaseName)
  }

}

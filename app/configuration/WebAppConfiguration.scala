package configuration

import org.springframework.data.mongodb.core.MongoTemplate

/**
 * Created by Szymek.
 */
object WebAppConfiguration {

  val databaseName: String = "TMS_DB"
  val testDatabaseName: String = "TEST_TMS_DB"

  def mongoTemplate: MongoTemplate = {
    val mongoDBConfiguration: MongoDBConfiguration = new MongoDBConfiguration(databaseName)
    new MongoTemplate(mongoDBConfiguration.mongo(), databaseName)
  }

  def mongoTemplateTest: MongoTemplate = {
    val mongoDBConfiguration: MongoDBConfiguration = new MongoDBConfiguration(testDatabaseName)
    new MongoTemplate(mongoDBConfiguration.mongo(), testDatabaseName)
  }

}

package configuration

import com.mongodb.casbah.MongoClient

/**
 * Created by Szymek Seget on 22.05.15.
 */
object CasbahMongoDBConfiguration {

  def mongo() = {
    val mongoClient = MongoClient("localhost", 27017)
    val db = mongoClient("TEST_TMS_DB")
    db
  }

}

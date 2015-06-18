package configuration

import com.mongodb.MongoClientURI
import com.mongodb.casbah.MongoClient

/**
 * Created by Szymek Seget on 22.05.15.
 */
object CasbahMongoDBConfiguration {

  var databaseName = ""
  def mongo() = {
    val URI = System.getenv("MONGO_URI")
    if(URI == null) {
      databaseName = "TMS_DB"
      val mongoClient = MongoClient("localhost", 27017)
      val db = mongoClient(databaseName)
      db
    } else {
      databaseName = "tms_db"
      val uri = new MongoClientURI("mongodb://tms:tms123@ds061928.mongolab.com:61928/tms_db")
      val mongoClient = MongoClient(uri)
      val db = mongoClient(databaseName)
      db
    }
  }

}

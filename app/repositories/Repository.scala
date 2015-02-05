package repositories

import configuration.WebAppConfiguration
import org.springframework.data.mongodb.core.query.Query


/**
 * Created by Szymek.
 */
trait Repository[T] {

  val collectionName:String
  val clazz:Class[T]

  protected var mongoTemplate = WebAppConfiguration.mongoTemplate

  /**
   * Change normal db to test db.
   */
  def initTest() = {
    mongoTemplate = WebAppConfiguration.mongoTemplateTest
  }

  /**
   * Change test db to normal db.
   */
  def finishTest() = {
    mongoTemplate = WebAppConfiguration.mongoTemplate
  }

  def remove[T](obj: T) = {
    mongoTemplate.remove(obj, collectionName)
  }


  def insert[T](obj: T) = {
    mongoTemplate.save(obj, collectionName)
  }

  def find(query: Query) = {
    mongoTemplate.find(query, clazz, collectionName)
  }
}

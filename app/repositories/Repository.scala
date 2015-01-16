package repositories

import configuration.WebAppConfiguration

/**
 * Created by Szymek.
 */
trait Repository {

  protected var mongoTemplate = WebAppConfiguration.mongoTemplate

  /**
   * Change normal db to test db.
   */
  def initTest()={
    mongoTemplate = WebAppConfiguration.mongoTemplateTest
  }

  /**
   * Change test db to normal db.
   */
  def finishTest()={
    mongoTemplate = WebAppConfiguration.mongoTemplate
  }

}

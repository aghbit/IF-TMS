package models.statistics.user.volleyballs

import models.exceptions.NegativeValueException
import models.statistics.user.UserStatistics

/**
 * Created by krzysiek.
 */
trait VolleyballsStatistics extends UserStatistics {
  var numberOfPoints:Int

  def addNumberOfPoints(points:Int):Unit = {
    if(points<0){
      throw new NegativeValueException("Can't add. Positive value required.")
    }
    numberOfPoints += points
  }
}
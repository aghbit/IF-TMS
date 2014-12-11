package models.statistics.volleyball.beachvolleyball

import models.team.Team
import reactivemongo.bson.BSONObjectID
import scala.collection.immutable.List
import scala.collection.mutable.Map

/**
 * Created by krzysiek.
 */
class TournamentStatistics(val discipline:String,
                           val pointUnit:String,
                           var numberOfMatches:Int,
                           var numberOfSets:Int,
                           var numberOfPoints:Int) extends BeachVolleyballStats{
  var numberOfTieBreaks:Int = 0
  var results:Map[BSONObjectID,Array[Int]] = Map() // Team -> [wins,losses]

  def generateResultsTable(teams:List[Team]):Unit = {
    for(team <- teams){
      results += (team._id -> Array(0,0))
    }
  }

  def updateResultsTable(team:BSONObjectID, i:Int): Unit ={ // i: 0 - wins,  1 - losses
    if(results.contains(team)){
      results.get(team).get(i) += 1
    }
  }

  def addNumberOfTieBreaks():Unit = {
    numberOfTieBreaks += 1
  }
  def getNumberOfTieBreaks:Int = {
    numberOfTieBreaks
  }

}

package repositories

import com.mongodb.DBObject
import com.mongodb.casbah.commons.conversions.scala._
import configuration.CasbahMongoDBConfiguration
import models.tournament.Tournament
import repositories.converters.TournamentDBObjectConverter


/**
 * Created by Szymek.
 */
class TournamentRepository{

  val collectionName: String = "Tournaments"
  val collection = CasbahMongoDBConfiguration.mongo().apply(collectionName)
  RegisterJodaTimeConversionHelpers()


  @throws[IllegalArgumentException]
  def insert(tournament: Tournament) = {
    if(!tournament.isReadyToSave){
      throw new IllegalArgumentException("Tournament is not ready to save (Probably contains illegal values)!")
    }else{
      collection.save(TournamentDBObjectConverter.toDbObject(tournament))
    }
  }



  def findOne(criteria: DBObject):Option[Tournament] = {
    collection.findOne(criteria) match {
      case Some(obj) => {
        TournamentDBObjectConverter.fromDbObject(obj)
      }
      case None => None
    }
  }

  def find(criteria:DBObject):List[Tournament] = {
    val tournaments = for(tournament <- collection.find(criteria)) yield
      TournamentDBObjectConverter.fromDbObject(tournament)
    tournaments.toList.flatten
  }

  def remove(t:Tournament) = ???

  def dropCollection() = collection.dropCollection()

}

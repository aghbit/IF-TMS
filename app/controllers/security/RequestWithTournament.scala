package controllers.security

import models.enums.ListEnum
import models.tournament.Tournament
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import play.api.mvc._
import repositories.TournamentRepository
import scala.collection.JavaConversions._


import scala.concurrent.Future

class RequestWithTournament[A](val tournament: Tournament,
                                 request: Request[A]) extends WrappedRequest[A](request){

}

object TournamentAction extends ActionBuilder[RequestWithTournament] with Controller {

  private val tournamentRepository = new TournamentRepository()
  private var tournamentId:String = _

  def apply(id:String) = {
    tournamentId = id
    this
  }

  def invokeBlock[A](request: Request[A], block: (RequestWithTournament[A]) => Future[Result]) = {
    if(ObjectId.isValid(tournamentId)){
      val id = new ObjectId(tournamentId)
      val token = TokenImpl(request.headers.get("token").get)
      val userId = token.getUserID
      val query = new Query(Criteria where "_id" is id and "staff.admin" is userId)
      val tournaments = tournamentRepository.find(query)
      if(!tournaments.isEmpty){
        block(new RequestWithTournament[A](tournaments.get(ListEnum.head), request))
      }else{
        Future.successful(NotFound("You can't see this tournament or it doesn't exist!"))
      }
    }else{
      Future.successful(NotFound("Wrong tournament id!"))
    }
  }

  override protected def composeAction[A](action: Action[A]): Action[A] = new Authorization[A](action)

}


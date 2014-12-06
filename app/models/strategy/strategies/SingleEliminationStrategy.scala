package models.strategy.strategies

import models.strategy.{MatchStructure, TournamentStrategy}
import models.strategy.matches.Match
import models.team.Team
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class SingleEliminationStrategy(val ListOfTeams:List[Team],
                                val isThirdPlaceMatch:Boolean,
                                val isSeeding:Boolean) extends EliminationStrategy {

  override val ListOfMatches: List[Match] = ???
  override val View: MatchStructure = _

  override def draw[Option: String]: Unit = ???

  override def setScore(id: Option[BSONObjectID], score: String): Unit = ???

  override def getView(): MatchStructure = ???

  override def getOrder(): List[Team] = ???

  override def attachNumberOfTeams(): Int = ???

}
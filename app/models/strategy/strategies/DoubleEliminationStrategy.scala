package models.strategy.strategies


import models.strategy.Tree.EliminationTree
import models.strategy.{TournamentStrategy, Match, Score}
import models.team.Team
import reactivemongo.bson.BSONObjectID


/**
 * Created by Szymek.
 */
class DoubleEliminationStrategy (val ListOfTeams:List[BSONObjectID],
                                 val isSeeding:Boolean) extends TournamentStrategy{
  override var tree: EliminationTree = _

  override def populateTree(list: List[Team]): Unit = ???

  override def getNextMatch(): Match = ???

  override def generateTree(): EliminationTree = ???

  override def updateTree(m: Match): Unit = ???
}
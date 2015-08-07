package models.strategy.eliminationtrees

import models.strategy.{Match, EliminationTree}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsObject

/**
 * Created by Szymek Seget on 08.08.15.
 */
class SingleEliminationTree (override val _id:ObjectId,
                            override val teamsNumber:Int,
                            override val tournamentType:TournamentType,
                            override val root:TreeNode) extends EliminationTree {

  override def getMatchesInNthRound(n: Int): List[Match] = ???

  override def mapMatches(f: (Match) => Match): Unit = ???

  override def addLoserToSecondQF(loser: Option[Team], prevMatchDepth: Int): Unit = ???

  override def setQFs(first: TreeNode, second: TreeNode, third: TreeNode, forth: TreeNode): Unit = ???

  override def foreachTreeNodes(f: (TreeNode) => Unit): Unit = ???

  override def toJson(): JsObject = ???

  override def addLoserToThirdQF(loser: Option[Team], prevMatchDepth: Int): Unit = ???

  override def depth: Int = ???

  override def getNode(matchID: Int): TreeNode = ???

  override def iterator: Iterator[TreeNode] = ???
}

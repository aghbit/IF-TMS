package models.strategy

import models.strategy.eliminationtrees.TreeNode
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsObject


/**
 * Created by Szymek Seget on 2015-02-22.
 */
trait EliminationTree extends Iterable[TreeNode]{

  val _id:ObjectId
  val teamsNumber:Int
  val root:TreeNode
  val tournamentType:TournamentType
  var firstQF:TreeNode = _
  var secondQF:TreeNode = _
  var thirdQF:TreeNode = _
  var forthQF:TreeNode = _


  def depth:Int

  def mapMatches(f: Match => Match):Unit

  def getMatchesInNthRound(n:Int):List[Match]

  def getNode(matchID: Int):TreeNode

  def foreachTreeNodes(f:TreeNode => Unit)

  def toJson():JsObject

}
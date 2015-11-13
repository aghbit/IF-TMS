package models.strategy.structures

import models.strategy.structures.eliminationtrees.TreeNode
import models.strategy.{EliminationStrategy, EliminationStructure, Match}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsObject


/**
 * Created by Szymek Seget on 2015-02-22.
 */
trait EliminationTree extends Iterable[TreeNode] with EliminationStructure{

  val _id:ObjectId
  val teamsNumber:Int
  val root:TreeNode
  val tournamentType:TournamentType
  val strategy:EliminationStrategy
  var firstQF:TreeNode = _
  var secondQF:TreeNode = _
  var thirdQF:TreeNode = _
  var forthQF:TreeNode = _


  def depth:Int

  def mapMatches(f: Match => Match):Unit

  def getMatchesInNthRound(n:Int):List[Match]

  def getNode(matchID: Int):TreeNode

  def foreachNode(f:TreeNode => Unit)

  def toJson():JsObject

}
package models.strategy

import models.strategy.eliminationtrees.TreeNode
import models.team.Team


/**
 * Created by Szymek Seget on 2015-02-22.
 */
trait EliminationTree {
  var firstQF:TreeNode = _


  var secondQF:TreeNode = _
  var thirdQF:TreeNode = _
  var forthQF:TreeNode = _

  def addLoserToSecondQF(loser: Option[Team], prevMatchDepth:Int):Unit

  def addLoserToThirdQF(loser: Option[Team], prevMatchDepth:Int):Unit

  def depth:Int

  def mapMatches(f: Match => Match):Unit

  def getMatchesInNthRound(n:Int):List[Match]

  def getNode(matchID: Int):TreeNode

  def setQFs(first:TreeNode, second:TreeNode, third:TreeNode, forth:TreeNode)

}
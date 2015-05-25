package models.strategy.eliminationtrees

import models.strategy.{Match, EliminationTree}
import models.team.Team
import models.tournament.tournamenttype.TournamentType

/**
 * Created by Szymek Seget on 25.05.15.
 */
class DoubleEliminationTree(teams: List[Team], tournamentType: TournamentType) extends EliminationTree {

  var root:TreeNode = constructorHelper(0)

  val teamsNumber = teams.length

  val teamsList = teams

  /**
   * Represents max depth of elimination tree. Counts from 0 (final), e.g.
   * For 32 teams depth equals 8.
   */
  val depth = (Math.log(teamsNumber)/Math.log(2)).asInstanceOf[Int] + 2

  /**
   * Represents max depth of elimination tree without losers. So it is only depth of 1 and 3
   * quarterfinals.
   */
  val winnerDepth = depth - 2

  private def constructorHelper(depthCounter:Int):TreeNode = {
    if(depthCounter == winnerDepth){
      new TreeNode(None, None, Match(None, None, tournamentType))
    }else{
      new TreeNode(
        Some(constructorHelper(depthCounter+1)),
        Some(constructorHelper(depthCounter+1)),
        Match(None, None, tournamentType)
      )
    }
  }

  def getMatchesInNthRound(n:Int):List[Match] = {
    def helper(n:Int, root:Option[TreeNode]): List[Match] ={
      if(root.isEmpty) {
        List()
      }else if(n==0){
        List(root.get.value)
      }else {
        helper(n-1, root.get.left) ::: helper(n-1, root.get.right)
      }
    }
    helper(n, Some(root))
  }

  private def addLoserAdditionalPart() = {
    val matches = getMatchesInNthRound(depth)
  }




}

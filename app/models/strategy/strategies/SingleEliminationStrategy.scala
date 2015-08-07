package models.strategy.strategies


import models.strategy.eliminationtrees.{SingleEliminationTree, TreeNode}
import models.strategy.{Match, EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

/**
 * Created by Szymek
 */

object SingleEliminationStrategy extends EliminationStrategy{

  override def initEmptyTree(id:ObjectId, teamsNumber: Int, tournamentType: TournamentType): EliminationTree = {
    require(teamsNumber >=2, "Too few teams to generate Single Elimination Tree. Should be >=2.")

    val leafsNumber = countLeaf(teamsNumber)
    val treeDepth = countTreeDepth(leafsNumber)

    val greatFinal = generateEmptyTree(treeDepth, tournamentType)

    new SingleEliminationTree(id, teamsNumber, tournamentType, greatFinal)
  }

  override def generateTree(teams: List[Team], tournamentType: TournamentType, tournamentID: ObjectId): EliminationTree = ???

  override def updateMatchResult(eliminationTree: EliminationTree, m: Match): EliminationTree = ???

  private def generateEmptyTree(treeDepth: Int, tournamentType: TournamentType) = {
    def helper(depth:Int, i:Int):TreeNode = {
      if(treeDepth==0){
        new TreeNode(None, None, Match(None, None, tournamentType), i)
      }else{
        new TreeNode(
        Some(helper(depth-1, i+1)),
        Some(helper(depth-1, i+1)),
        Match(None, None, tournamentType),
        i
        )
      }
    }
    helper(treeDepth, 0)
  }

  /**
   * Returns number of rounds from final to first matches. e.g.
   * For 8 leafs returns 3
   * For 16 leafs returns 4
   * @param leafNumber
   * @return Int number of rounds
   */
  def countTreeDepth(leafNumber: Int) = {
    (Math.log(leafNumber)/Math.log(2)).asInstanceOf[Int]
  }

  /**
   * Returns how many teams leafs are needed to populate n Teams.
   * e.g n=8 returns 8, n=17 returns 32.
   * @param n
   * @return Int - number of leafs
   */
  def countLeaf(n: Int) = {
    var result = 2
    while(result<n){
      result = result*2
    }
    result
  }
}
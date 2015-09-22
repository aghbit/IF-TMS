package models.strategy.strategies


import models.strategy.structures.EliminationTree
import models.strategy.{EliminationStructure, Match, EliminationStrategy}
import models.team.Team
import models.strategy.structures.eliminationtrees.TreeNode
import models.tournament.tournamenttype.TournamentType
import models.strategy.structures.eliminationtrees.DoubleEliminationTree
import org.bson.types.ObjectId

import scala.util.Random
;

/**
 * Created by Szymek.
 */

object DoubleEliminationStrategy extends EliminationStrategy{

  private def updateMatchResultHelper(eliminationTree: DoubleEliminationTree, m:Match):EliminationTree = {
    val winner = m.getWinner()
    val loser = m.getLoser()
    val node = eliminationTree.getNode(m.id)
    node.value = m
    node.parent match {
      case Some(n) => n.value.addParticipant(winner)
      case None => // Koniec turnieju
    }
    if(eliminationTree.firstQF.contains(node)){
      eliminationTree.addLoserToThirdQF(loser, node.depth)
    }else if(eliminationTree.forthQF.contains(node)){
      eliminationTree.addLoserToSecondQF(loser, node.depth)
    }
    eliminationTree
  }

  private def populateTree(eliminationTree: DoubleEliminationTree, teams: List[Team]): EliminationTree = {
    val teamsIterator = Random.shuffle(teams.indices.toList).toIterator
    val matches = eliminationTree.getMatchesInNthRound(eliminationTree.winnersTreeDepth)
    val firstQF = matches.take(eliminationTree.leafsNumber/4)
    val secondQF = matches.drop(matches.length - eliminationTree.leafsNumber / 4)
    val matchesToPopulate = firstQF ::: secondQF
    val matchesToPopulateIterator = matchesToPopulate.iterator
    while (matchesToPopulateIterator.hasNext){
      val matchToPopulate = matchesToPopulateIterator.next()
      if(teamsIterator.hasNext){
        matchToPopulate.host = Some(teams(teamsIterator.next()))
      }
      if(teamsIterator.hasNext){
        matchToPopulate.guest = Some(teams(teamsIterator.next()))
      }
    }
    eliminationTree
  }

  /**
   * Generates double elimination tree, with populated teams. Teams are populated randomly.
   * Matches are numbered (Like bfs. e.g Final 0, left SF 1, right SF 2 ...).
   *
   * @param teams - The tree is populated by this teams randomly.
   * @param tournamentType - Important, because algo has to know which type of score to create.
   * @return double elimination tree
   */
  @throws(classOf[IllegalArgumentException])
  override def generate(teams: List[Team], tournamentType: TournamentType, tournamentID:ObjectId): EliminationTree = {
    require(teams.length>=8, "Too few teams to generate DoubleEliminationTree. Should be >=8.")

    val eliminationTree = initEmpty(tournamentID, teams.length, tournamentType)

    populateTree(eliminationTree, teams)
  }

  /**
   * Returns reference to TreeNode, which is a root of right QF Looser tree.
   * The difference between looser and winner tree means that winner tree is full binary
   * tree, in contrast, looser tree has structure like this:
   *          (TreeNode)
   *              |
   *          (TreeNode)
   *           /     \
   *    (TreeNode) (TreeNode)
   *         |         |
   *    (TreeNode) (TreeNode)
   * etc.
   * This tree is a full binary tree.
   * @param looserTreeDepth
   * @param tournamentType
   * @return TreeNode - reference to looser QF root.
   */
  private def generateLosersQF(looserTreeDepth: Int, tournamentType: TournamentType) = {
    def helper(depth:Int, i:Int):TreeNode = {
      if(depth == 0) {
        new TreeNode(None, None, Match(None, None, tournamentType), i+2)
      }else if(i%2 == 0) {
        new TreeNode(
          None,
          Some(helper(depth-1, i + 1)),
          Match(None, None, tournamentType),
          i+2
        )
      }else {
        new TreeNode(
          Some(helper(depth-1, i+1)),
          Some(helper(depth-1, i+1)),
          Match(None, None, tournamentType),
          i+2
        )
      }
    }
    helper(looserTreeDepth-2, 0)
  }

  /**
   * Returns reference to TreeNode, which is a root of right QF tree.
   * This tree is a full binary tree.
   * @param winnerTreeDepth
   * @param tournamentType
   * @return TreeNode - reference to QF root.
   */
  private def generateWinnersQF(winnerTreeDepth: Int, tournamentType: TournamentType) = {
    def helper(depth:Int, i:Int):TreeNode = {
      if(depth == 0) {
        new TreeNode(None, None, Match(None, None, tournamentType), i+2)
      }else {
        new TreeNode(
        Some(helper(depth-1, i+1)),
        Some(helper(depth-1, i+1)),
        Match(None, None, tournamentType),
        i+2
        )
      }
    }
    helper(winnerTreeDepth-2, 0)
  }

  /**
   * Returns number of rounds from final to first match on looser side. e.g.
   * For 3 returns 3
   * For 4 returns 5
   * For 5 returns 7
   * For 6 returns 9
   * @param winnerTreeDepth
   * @return Int number of rounds
   */
  def countLoserDepth(winnerTreeDepth: Int) = {
    2*winnerTreeDepth-3
  }
  
  /**
   * Returns number of rounds from final to first matches. e.g.
   * For 8 leafs returns 3
   * For 16 leafs returns 4
   * @param leafNumber
   * @return Int number of rounds
   */
  def countWinnerDepth(leafNumber: Int) = {
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

  override def initEmpty(id:ObjectId, teamsNumber: Int, tournamentType: TournamentType): DoubleEliminationTree = {
    require(teamsNumber>=8, "Too few teams to generate DoubleEliminationTree. Should be >=8.")

    val leafNumber = countLeaf(teamsNumber)
    val winnerTreeDepth = countWinnerDepth(leafNumber)
    val loserTreeDepth = countLoserDepth(winnerTreeDepth)

    val winnersQF1 = generateWinnersQF(winnerTreeDepth, tournamentType)
    val winnersQF2 = generateWinnersQF(winnerTreeDepth, tournamentType)

    val losersQF1 = generateLosersQF(loserTreeDepth, tournamentType)
    val losersQF2 = generateLosersQF(loserTreeDepth, tournamentType)

    val semiFinal1 = new TreeNode(Some(winnersQF1), Some(losersQF1), Match(None, None, tournamentType), 1)
    val semiFinal2 = new TreeNode(Some(losersQF2), Some(winnersQF2), Match(None, None, tournamentType), 1)

    val greatFinal = new TreeNode(Some(semiFinal1), Some(semiFinal2), Match(None, None, tournamentType), 0)

    val eliminationTree = new DoubleEliminationTree(id, teamsNumber, tournamentType, greatFinal)

    eliminationTree.setQFs(winnersQF1, losersQF1, losersQF2, winnersQF2)

    eliminationTree
  }

  override def updateMatchResult(eliminationTree: EliminationStructure, m: Match): EliminationTree = {
    require(eliminationTree.isInstanceOf[DoubleEliminationTree], "Double Elimination Strategy needs Double Elimination Tree.")
    updateMatchResultHelper(eliminationTree.asInstanceOf[DoubleEliminationTree], m);
  }
}
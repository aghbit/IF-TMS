package models.strategy.strategies


import models.strategy.eliminationtrees.{SingleEliminationTree, TreeNode}
import models.strategy.{Match, EliminationTree, EliminationStrategy}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

import scala.util.Random

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

  /**
   * Generates single elimination tree, with populated teams. Teams are populated randomly.
   * Matches are numbered (Like bfs. e.g Final 0, left SF 1, right SF 2 ...).
   *
   * @param teams - The tree is populated by this teams randomly.
   * @param tournamentType - Important, because algo has to know which type of score to create.
   * @return single elimination tree
   */
  override def generateTree(teams: List[Team], tournamentType: TournamentType, tournamentID: ObjectId): EliminationTree = {
    require(teams.length >=2, "Too few teams to generate Single Elimination Tree. Should be >=2.")

    val eliminationTree = initEmptyTree(tournamentID, teams.length, tournamentType)

    populateTree(eliminationTree, teams)
  }

  def populateTree(eliminationTree: EliminationTree, teams: List[Team]): EliminationTree = {
    val teamsIterator = Random.shuffle(teams.indices.toList).iterator
    val depth: Int = eliminationTree.depth
    val matches = eliminationTree.getMatchesInNthRound(depth)
    val matchesIterator = matches.iterator
    while(matchesIterator.hasNext){
      val matchToPopulate = matchesIterator.next()
      if(teamsIterator.hasNext) {
        matchToPopulate.host = Some(teams(teamsIterator.next()))
      }
      if(teamsIterator.hasNext){
        matchToPopulate.guest = Some(teams(teamsIterator.next()))
      }
    }
    eliminationTree
  }


  override def updateMatchResult(eliminationTree: EliminationTree, m: Match): EliminationTree = {
    require(eliminationTree.isInstanceOf[SingleEliminationTree], "Single Elimination Strategy needs Single Elimination Tree.")
    updateMatchResultHelper(eliminationTree.asInstanceOf[SingleEliminationTree], m)
  }

  private def updateMatchResultHelper(tree: SingleEliminationTree, m: Match): EliminationTree = {
    val winner = m.getWinner()
    val loser = m.getLoser()
    val node = tree.getNode(m.id)
    node.value = m
    node.parent match {
      case Some(n) => n.value.addTeam(winner)
      case None => // Koniec turnieju
    }
    tree
  }

  private def generateEmptyTree(treeDepth: Int, tournamentType: TournamentType) = {
    def helper(depth:Int, i:Int):TreeNode = {
      if(depth==0){
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
    helper(treeDepth-1, 0)
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
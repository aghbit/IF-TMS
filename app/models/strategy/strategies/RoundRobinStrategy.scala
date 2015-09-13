package models.strategy.strategies

import models.strategy.structures.EliminationTable
import models.strategy.structures.eliminationtables.{TableNode, RoundRobinTable}
import models.strategy.{EliminationStrategy, EliminationStructure, Match}
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId

import scala.util.Random

/**
 * Created by Szymek Seget on 06.09.15.
 */
object RoundRobinStrategy extends EliminationStrategy {

  override def initEmpty(id: ObjectId,
                         teamsNumber: Int,
                         tournamentType: TournamentType): EliminationTable = {
    require(teamsNumber>=2, "Too few teams to generate Round Robin Table. Should be >=2.")
    val table = new RoundRobinTable(id, tournamentType, teamsNumber)
    for(i <- 0 until 2*table.tableSize-1) {
      val diagonal: List[TableNode] = table.getTableDiagonal(i)
      diagonal.foreach(t => t.round = (i%(teamsNumber-1))+1)
      }
    for(i <- 0 until 2*table.tableSize-1) {
      val diagonal: List[TableNode] = table.getRevengeTableDiagonal(i)
      diagonal.foreach(t => t.round = (i%(teamsNumber-1))+1+table.numberOfRoundsWithoutRevenge)
    }
    table
  }


  @throws(classOf[IllegalArgumentException])
  override def generate(teams: List[Team],
                        tournamentType: TournamentType,
                        tournamentID: ObjectId): EliminationTable = {
    require(teams.size>=2, "Too few teams to generate Round Robin Table. Should be >=2.")

    val table = initEmpty(tournamentID, teams.size, tournamentType)
    val shuffledTeams = Random.shuffle(teams)
    table.foreachNode(populate(shuffledTeams, tournamentType))

    table
  }

  override def updateMatchResult(eliminationTable: EliminationStructure, updatedMatch: Match): EliminationTable = {
    require(eliminationTable.isInstanceOf[EliminationTable], "Round Robin Strategy requires Elimination Table")
    val table = eliminationTable.asInstanceOf[EliminationTable]
    table.foreachNode(node =>
      for(
        m <- node.value
        if m.id.equals(updatedMatch.id)
      ) node.value = Some(updatedMatch)
    )
    table
  }

  private def populate(shuffledTeams:List[Team], tournamentType: TournamentType):TableNode => Unit = {
    node => {
      var host: Some[Team] = Some(shuffledTeams(node.y))
      var guest: Some[Team] = Some(shuffledTeams(node.x))
      var rest:Int = 0
      if(node.revenge){
        rest = 1
      }
      if(node.y == node.x && node.y%2 == rest){
        host = Some(shuffledTeams.last)
      }else if (node.y == node.x) {
        guest = Some(shuffledTeams.last)
      }else if (node.revenge) {
        val tmp = host
        host = guest
        guest = tmp
      }
      node.value = Some(Match(
        host,
        guest,
        tournamentType))
    }
  }
}

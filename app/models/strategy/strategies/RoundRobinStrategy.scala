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
      val diagonal: List[TableNode] = table.getDiagonal(i)
      diagonal.foreach(t => t.round = (i%(teamsNumber-1))+1)
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

    table.foreachNode(populate(false, shuffledTeams, tournamentType))

    table

  }

  override def updateMatchResult(eliminationTable: EliminationStructure, m: Match): EliminationTable = ???

  private def populate(isRevenge:Boolean, shuffledTeams:List[Team], tournamentType: TournamentType):TableNode => Unit = {
    node => {
      var host: Some[Team] = Some(shuffledTeams(node.y))
      val guest: Some[Team] = Some(shuffledTeams(node.x))
      var rest:Int = 0
      if(isRevenge){
        rest = 1
      }
      if(node.y == node.x && node.y%2 == rest){
        host = Some(shuffledTeams.last)
      }
      node.value = Some(Match(
        host,
        guest,
        tournamentType))
    }
  }
}

package models.strategy.structures

import models.strategy.structures.eliminationtables.TableNode
import models.strategy.{Match, EliminationStrategy, EliminationStructure}
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsObject

/**
 * Created by Szymek Seget on 06.09.15.
 */
trait EliminationTable extends Iterable[TableNode] with EliminationStructure {

  val _id:ObjectId
  val teamsNumber:Int
  val tournamentType:TournamentType
  val strategy:EliminationStrategy

  def toJson:JsObject

  def mapMatches(f: Match => Match):Unit

  def getMatchesInNthRound(n:Int):List[Match]

  def foreachNode(f:TableNode => Unit):Unit

  def getTableDiagonal(k:Int):List[TableNode]
  
  def getRevengeTableDiagonal(k:Int):List[TableNode]
}

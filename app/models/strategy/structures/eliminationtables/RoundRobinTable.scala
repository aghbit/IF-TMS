package models.strategy.structures.eliminationtables

import models.strategy.Match
import models.strategy.strategies.RoundRobinStrategy
import models.strategy.structures.EliminationTable
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.JsObject

/**
 * Created by Szymek Seget on 06.09.15.
 */
class RoundRobinTable(override val _id:ObjectId,
                     override val tournamentType: TournamentType,
                     override val teamsNumber:Int) extends EliminationTable{
  override val strategy = RoundRobinStrategy


  /**
   * Counts table size. Check https://en.wikipedia.org/wiki/Round-robin_tournament for more information.
   */
  val tableSize = teamsNumber - (1-teamsNumber%2)


  private val table = Array.tabulate[TableNode](tableSize, tableSize){
    (y, x) => new TableNode(None, 0, y, x)
  }


  val numberOfMatches:Int = (teamsNumber*(teamsNumber-1))/2

  val numberOfRounds:Int = {
    if(teamsNumber%2 == 0) {
      teamsNumber-1
    }else{
      teamsNumber
    }
  }

  val gamesPerRound:Int = {
    if(teamsNumber%2 == 0) {
      teamsNumber/2
    }else{
      (teamsNumber-1)/2
    }
  }


  override def getDiagonal(k: Int):List[TableNode] = {
    (for(
      i <- table.indices;
      j <- table(i).indices
      if (i+j) == k
    ) yield table(i)(j)).toList
  }

  override def toJson: JsObject = ???

  override def iterator: Iterator[TableNode] = {
    (for(i <- 0 until numberOfRounds) yield getNodesInNthRound(i)).flatten.iterator
  }

  private def getNodesInNthRound(k:Int):List[TableNode] = {
    (for(
      i <- table.indices;
      j <- table(i).indices
      if table(i)(j).round == k
    ) yield table(i)(j)).toList
  }

  override def mapMatches(f: (Match) => Match): Unit = {
    for (
      row <- table
    ) yield for(
        tableNode <- row
      ) tableNode.value match {
        case Some(value) => tableNode.value = Some(f(value))
        case None => tableNode.value = None
      }
  }

  override def foreachNode(f:TableNode => Unit):Unit = {
    for(
      row <- table;
      node <- row
    ) f(node)
  }

  override def getMatchesInNthRound(n: Int): List[Match] = {
    ???
  }

  override def toString = {
    val builder = new StringBuilder
    foreachNode(node =>
      node.round match {
        case Some(r) => builder.append(r)
        case None =>
      })
    builder.result()
  }
}

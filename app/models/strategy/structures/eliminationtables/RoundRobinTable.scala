package models.strategy.structures.eliminationtables

import models.strategy.Match
import models.strategy.strategies.RoundRobinStrategy
import models.strategy.structures.EliminationTable
import models.team.Team
import models.tournament.tournamenttype.TournamentType
import org.bson.types.ObjectId
import play.api.libs.json.{Json, JsObject}

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

  private def createNode(x:Int,y:Int, revenge:Boolean)= {
    if(x>=y)
      Some(new TableNode(None, 0, y, x, revenge))
    else
      None
  }

  /**
   * table represents first part of season. This is upper triangular table (plus diagonal), in lower triangular
   * there are Nones. revengeTable represents second part of season. It is similar table to table, but host and
   * guest are swapped.
   */
  private val table = Array.tabulate[Option[TableNode]](tableSize, tableSize){(x,y) => createNode(x,y, false)}
  private val revengeTable = Array.tabulate[Option[TableNode]](tableSize, tableSize) {(x,y) => createNode(x,y, true)}


  val numberOfMatchesWithRevenge:Int = teamsNumber * (teamsNumber - 1)
  val numberOfMatchesWithoutRevenge:Int = numberOfMatchesWithRevenge/2

  val numberOfRoundsWithRevenge:Int = {
    if(teamsNumber%2 == 0) {
      (teamsNumber-1)*2
    }else{
      teamsNumber*2
    }
  }
  val numberOfRoundsWithoutRevenge:Int = numberOfRoundsWithRevenge/2

  val gamesPerRound:Int = {
    if(teamsNumber%2 == 0) {
      teamsNumber/2
    }else{
      (teamsNumber-1)/2
    }
  }

  private def getDiagonal(table:Array[Array[Option[TableNode]]], k:Int) = {
    (for(
      i <- table.indices;
      j <- table(i).indices
      if (i+j) == k;
      node <- table(i)(j)
    ) yield node).toList
  }

  override def getTableDiagonal(k: Int):List[TableNode] = getDiagonal(table, k)

  override def getRevengeTableDiagonal(k:Int):List[TableNode] = getDiagonal(revengeTable, k)

  override def iterator: Iterator[TableNode] = {
    (for(i <- 0 until numberOfMatchesWithRevenge) yield getNodesInNthRound(i)).flatten.iterator
  }

  private def getNodesInNthRound(k:Int):List[TableNode] = {
    var t = table
    if(k>numberOfRoundsWithoutRevenge) {
      t = revengeTable
    }
      (for(
        i <- t.indices;
        j <- t(i).indices;
        node <- t(i)(j)
        if node.round == k
      ) yield node).toList
  }

  private def mapMatchesInTable(table:Array[Array[Option[TableNode]]])(f:(Match)=>Match):Unit = {
    for (
      row <- table
    ) yield for(
      tableNode <- row;
      node <- tableNode
    ) node.value match {
        case Some(value) => node.value = Some(f(value))
        case None => node.value = None
      }
  }

  override def mapMatches(f: (Match) => Match): Unit = {
    mapMatchesInTable(table)(f)
    mapMatchesInTable(revengeTable)(f)
  }

  private def foreachNodeInTable(table:Array[Array[Option[TableNode]]])(f:(TableNode)=>Unit):Unit = {
    for(
      row <- table;
      node <- row;
      n <- node
    ) {
      f(n)
    }
  }

  override def foreachNode(f:TableNode => Unit):Unit = {
    foreachNodeInTable(table)(f)
    foreachNodeInTable(revengeTable)(f)
  }

  override def getMatchesInNthRound(n: Int): List[Match] = {
    var t = table
    if(n>numberOfRoundsWithoutRevenge){
      t=revengeTable
    }
    (for(
      i <- t.indices;
      j <- t(i).indices;
      node <- t(i)(j)
      if node.round == n;
      m <- node.value
    ) yield m).toList
  }

  override def toJson: JsObject = {
    val json = Json.obj(
      "type"->"RoundRobinTable",
      "numberOfMatchesWithRevenge" -> numberOfMatchesWithRevenge,
      "numberOfRoundsWithRevenge" -> numberOfRoundsWithRevenge,
      "numebrOfMatchesWithoutRevenge" -> numberOfMatchesWithoutRevenge,
      "numberOfRoundsWithoutRevenge" -> numberOfRoundsWithoutRevenge,
      "rounds" ->
        (for(i<- 1 to numberOfRoundsWithRevenge)
          yield Json.obj("id" -> i.toString,
            "matches" -> getMatchesInNthRound(i).map(m => m.toJson))
      ).toList
    )
    json
  }
}

package models.strategy.structures.eliminationtables

import models.strategy.Match

/**
 * Created by Szymek Seget on 06.09.15.
 */
class TableNode(var value:Option[Match],
                 var round:Int,
                 val y:Int,
                 val x:Int,
                 val revenge:Boolean) {

  def toJson = ???

  override def toString={
    val builder = new StringBuilder
    value match {
      case Some(m) => builder.append(m.toJson.toString())
    }
    builder.append(", round= " + round)
    builder.append(" [x,y]=["+x+","+y+"]")
    builder.append("revenge="+revenge)
    builder.result()
  }
}


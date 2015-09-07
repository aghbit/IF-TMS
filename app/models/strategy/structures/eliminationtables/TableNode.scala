package models.strategy.structures.eliminationtables

import models.strategy.Match

/**
 * Created by Szymek Seget on 06.09.15.
 */
class TableNode(var value:Option[Match],
                 var round:Int,
                 val y:Int,
                 val x:Int) {

  def toJson = ???
}


package models.Game

import models.strategy.Match
/**
 * Created by ludwik on 13.12.14.
 */
class Game(var right : Option[Game], var left:Option[Game],var parent:Option[Game], var value:Option[Match]){
  def this(){
    this(None,None,None,None)
  }
  def addNull(count : Int):Game = {
    if(count>0) {
      left = Some(Game())
      right = Some(Game())
      value = None
      left = Some(left.get.addNull(count - 1))
      left.get.parent = Some(this)
      right = Some(right.get.addNull(count - 1))
      right.get.parent = Some(this)
    }
    this
  }

}

object Game{
  def apply() = new Game()
}


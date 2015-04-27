package models.Game

import models.strategy.Match

/**
 * Created by ludwik on 13.12.14.
 */
class Game(var right: Option[Game] = None, var left: Option[Game] = None, var parent: Option[Game] = None, var value: Option[Match] = None) {

  def createFullEmptyTree(height: Int): Game = {
    if (height > 0) {
      left = Some(Game())
      right = Some(Game())
      left = Some(left.get.createFullEmptyTree(height - 1))
      left.get.parent = Some(this)
      right = Some(right.get.createFullEmptyTree(height - 1))
      right.get.parent = Some(this)
    }
    this
  }

}

object Game {
  def apply() = new Game()
}


package models.strategy


/**
 * Created by ludwik on 13.12.14.
 */
package object Tree {
  class Game(var right : Option[Game], var left:Option[Game],var parent:Option[Game], var value:Option[Match]){
    def this(){
      this(None,None,None,None)
    }
  }

  object Game{
    def apply() = new Game()
  }
  class EliminationTree(val root: Game){
    def this(){
      this(new Game())
    }
  }
}

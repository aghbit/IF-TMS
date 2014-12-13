package models.strategy


/**
 * Created by ludwik on 13.12.14.
 */
package object Tree {
   class Game(var right : Game, var left:Game,var parent:Game, var value:Match){
     def this(){
       this(null,null,null,null)
     }
   }
   class EliminationTree(val root: Game){
    def this(){
       this(new Game(null,null,null,null))
    }
  //  def getLatest():Game = {return new Game();}
  }
}


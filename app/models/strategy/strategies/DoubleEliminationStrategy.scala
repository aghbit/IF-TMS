package models.strategy.strategies


import reactivemongo.bson.BSONObjectID


/**
 * Created by Szymek.
 */
class DoubleEliminationStrategy (val ListOfTeams:List[BSONObjectID],
                                 val isSeeding:Boolean) {

//
//      override var matches:Map[Match,Int] = ???
//
//      override def draw:Unit = ???
//      override def getOrder():List[Team] = ???
//      override def attachNumberOfTeams():Int = ???
//
//      override def setScore(game: Match, score: Score): Unit = ???
}
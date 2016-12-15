package game

import scala.collection.mutable.ArrayBuffer

object LeaderBoard {
  
  var board = new ArrayBuffer[Int](5)
  
  def addScore(score:Int) = {
    var i = 0
    while ( i < board.size){
      if (score > board(i)) {
        board.insert(i, score)
        board.dropRight(1)
        i = board.size
        
      }
    }
      
   }
  
}
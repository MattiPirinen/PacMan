package game

import scala.collection.mutable.ArrayBuffer
import java.io._
import scala.io.Source

object LeaderBoard {
  
  /*
   * addScore(Int) to add a score to the leaderboard
   * deleteScores() to reset leaderboard
   * showLeaderBoard() returns array of scores you can display
   * other methods do not need to be used from outside this object  
   */
  
  private var board = ArrayBuffer.fill(5)("a:0")
  private val saveFile = "leaderboard.txt"
  
  def printToFile(f: File)(op: PrintWriter => Unit) {
  val p = new PrintWriter(f)
  try { op(p) } finally { p.close() }
}

  
  def addScore(score:String) = {
    var saved = 0
    loadScores()
    for(i <- 0 until board.size) {
      if (score.split(":")(1).toInt > board(i).split(":")(1).toInt && saved == 0){
        board.insert(i, score)
        saved = 1
        }
      }
    board = board.take(5)
    saveScores()
    }
    
    def saveScores() = {
      printToFile(new File("leaderboard.txt")){p =>
        board.foreach(p.println)
      }
    }
    
    def loadScores() = {
      val loadedScores = new ArrayBuffer[String]
      try {
      for (line <- Source.fromFile("leaderboard.txt").getLines()){
        loadedScores += line
      }
      board = loadedScores
      } catch {
        case nofile: java.io.FileNotFoundException => board = ArrayBuffer.fill(5)("a:0")
      }
      
    }
    
    def deleteScores() = {
      board = ArrayBuffer.fill(5)("a:0")
      saveScores()
    }
       
    def showLeaderBoard() = {
      loadScores()
      board
    }
}
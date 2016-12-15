package game

object testApp extends App {
  val teksti = "Seppo:5"
  val numero2 = "Teuvo:4"
  //LeaderBoard.deleteScores()
  LeaderBoard.addScore(teksti)
  LeaderBoard.addScore(numero2)
  LeaderBoard.saveScores
  
  println(LeaderBoard.showLeaderBoard().mkString("\n"))
}
package game

class GhostSlowFloor extends Spot {
  var color = "YELLOW"
  
  var canHaveItem = true
  
  var isUnPassable = false
  
  var hasPlayer = false
  
  override def ghostSpeed(ghost:Ghost) = {
    ghost.changeSpeed(5)
  }
  
  val image = Graphics.ghostSlowFloor
  
}
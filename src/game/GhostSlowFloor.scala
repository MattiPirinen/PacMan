package game


/*
 * A class that represents a spot type where ghosts are slowed when they walk over it
 */
class GhostSlowFloor extends Spot {
  var color = "YELLOW"
  
  var canHaveItem = true
  
  var isUnPassable = false
  
  var hasPlayer = false
  
  override def ghostSpeed(ghost:Ghost) = {
    ghost.changeSpeed(3)
  }
  
  val image = Graphics.ghostSlowFloor
  
}
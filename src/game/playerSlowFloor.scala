package game
/*
 * A class that represents a spot type where player is slowed when he/she walk over it
 */
class PlayerSlowFloor extends Spot {
  
  var color = "RED"
  
  var canHaveItem = true
  
  var isUnPassable = false
  
  var hasPlayer = false
  
  val image = Graphics.playerSlowFloor
  
  override def playerSpeed(player:Player) = {
    player.changeSpeed(3)
    
  
  }
    
}
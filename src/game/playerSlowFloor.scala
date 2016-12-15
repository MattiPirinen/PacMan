package game

class playerSlowFloor extends Spot {
  
  var color = "RED"
  
  var canHaveItem = true
  
  var isUnPassable = false
  
  var hasPlayer = false
  
  override def playerSpeed(player:Player) = {
    player.changeSpeed(5)
  }
}
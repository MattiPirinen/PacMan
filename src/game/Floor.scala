package game

/*
 * This class represents a floor which can have items in it
 */

class Floor extends Spot {
  
  var color = "CYAN"
  
  var canHaveItem = true
  
  var isUnPassable = false
  
  var hasPlayer = false
  
  val image = Graphics.floor

}

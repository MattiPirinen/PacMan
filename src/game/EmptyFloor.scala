package game

/*
 * This Class represents a floor which cannot have any items on it
 */
class EmptyFloor extends Spot {
  
  var color = "CYAN"
  
  var canHaveItem = false
  
  var isUnPassable = false
  
  var hasPlayer = false
  
  val image = Graphics.floor
 }

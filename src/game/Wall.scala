package game

/*
 * This class represents a "wall" type of spot in the gameField that cannot be
 * passed though
 */

class Wall extends Spot {
  
  var canHaveItem = false
  
  var isUnPassable = true
  
  var hasPlayer = false
  
  val image = Graphics.fence
 }

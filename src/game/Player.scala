package game

/* This class represents a player in the game world
 * 
 */

class Player(x: Int,y: Int, val gridMap: Array[Array[Spot]]) extends Character(x,y) {
  
  def canMoveTo(xPos: Int, yPos: Int) = {
    Math.abs(xPos - x) <= 1 && Math.abs(yPos - y) <= 1
  }
  
  var currentDirection = Vector(0,0)
  
  var nextDirection: Option[Vector[Int]] = None
  
  speed = 2
  

  /*
  def checkDirectionChange(inputDirection:String) = {
    
  }
  * 
  */
}



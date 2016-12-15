package game

/* This class represents a player in the game world
 * 
 */

class Player(x: Int,y: Int, val gridMap: Array[Array[Spot]]) extends Character(x,y) {
  
  
  //Checks can player move the to position
  def canMoveTo(xPos: Int, yPos: Int) = {
    Math.abs(xPos - x) <= 1 && Math.abs(yPos - y) <= 1
  }
  
  var currentDirection = Vector(0,0) //Current direction of the player
  
  var nextDirection: Option[Vector[Int]] = None //Direction where the player would like to go next
  
  this.speed = 1 //Speed of the player
  
}



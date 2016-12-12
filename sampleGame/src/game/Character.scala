package game


/**
 * The class 'Character' represents a character in the game world.
 * The character can be controlled by a person or by a computer.
 * @param x 	x-direction in the game world
 * @param y 	y-direction in the game world
 */

class Character(var x:Int, var y:Int) {

  var counter = 1 // counter that counts when to move character
  
  def move(newX: Int, newY: Int) = {
    
    this.x = newX
    this.y = newY
  }
  
  var speed = 2
  

  
  val directions: Map[String,Vector[Int]] = 
    Map("Down" ->  Vector( 0, 1),
      "Left" ->    Vector(-1, 0),
      "Up" ->      Vector( 0,-1), 
      "Right" ->   Vector( 1, 0))  
  
  
  /*Determines can player move to the direction given
   * @param direction The direction in which the conditions are checked
   * @param world  		World in which the character is located
   * @param cellSize 	How many pixels is one cell in the world */
  def isThereNoWall(direction: Vector[Int], world: Array[Array[Spot]], cellSize: Int): Boolean = {
      
      var i = true
      var j = true
      
      
      val addX = direction(0)
      val addY = direction(1)
      
      //If player wants to go to X-direction checks is there a wall in X direction
      if (addX != 0) {
        if (addX >= 0) {
          i = !world((this.x + cellSize)/cellSize) (this.y/cellSize ).unPassable &&
            !world((this.x + cellSize)/cellSize) ((this.y+cellSize-1)/cellSize ).unPassable

        }  else { 
          i = !world((this.x-1)/cellSize)(this.y/cellSize ).unPassable &&
              !world((this.x-1)/cellSize)((this.y+cellSize-1)/cellSize ).unPassable
        }
      } 
      
      //If player wants to go to X-direction checks is there a wall in Y direction
      else if (addY != 0) {
        if (addY >= 0) {
          j = !world(this.x/cellSize)((this.y + cellSize) / cellSize).unPassable &&
            !world((this.x+cellSize-1)/cellSize)((this.y + cellSize) / cellSize).unPassable
        } else  {
          j = !world(this.x/cellSize)((this.y-1)/cellSize).unPassable && 
            !world((this.x+cellSize-1)/cellSize)((this.y-1) / cellSize).unPassable 
        }
      }
      i && j
      
  }
}
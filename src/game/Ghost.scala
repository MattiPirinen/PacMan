package game

import scala.collection.mutable.Buffer
import java.awt.Color


/*
 * This abstract class includes the variables and methods which are general to each ghost
 * if different kind of ghosts would be made.
 */
abstract class Ghost(x:Int, y:Int, val gridMap: Array[Array[Spot]], val cellSize: Int) extends Character(x,y) {
  
  val name:String //Name of the ghost
  

  
  val vectorDirections = directions.toVector.map(_._2) //All the directions ghost can move
  var currentDirection = Vector(0,0) // a variable where current direction will be saved
  
  
  //Chooses the direction where to go next
  def chooseDirection(firstX: Int, firstY:Int,Another: Character): Unit
  
  
  //Finds all available directions
  def findDirections: Buffer[Vector[Int]] = {
    var available = Buffer[Vector[Int]]()
    for (direction <- this.vectorDirections) {
      if (isThereNoWall(direction, this.gridMap, this.cellSize) &&
          direction.map(-1*_) != currentDirection) available += direction
    }
    available
  }
}
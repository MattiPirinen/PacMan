package game

import scala.collection.mutable.Buffer
import java.awt.Color


abstract class Ghost(x:Int, y:Int, val gridMap: Array[Array[Spot]], val cellSize: Int) extends Character(x,y) {
  

  
  var name = "Ghost"
  
  val vectorDirections = directions.toVector.map(_._2)
  var currentDirection = Vector(0,0)
  
  def chooseDirection(firstX: Int, firstY:Int,Another: Character): Unit
  
  def findDirections: Buffer[Vector[Int]] = {
    var available = Buffer[Vector[Int]]()
    for (direction <- this.vectorDirections) {
      if (isThereNoWall(direction, this.gridMap, this.cellSize) &&
          direction.map(-1*_) != currentDirection) available += direction
    }
    available
  }
  
  def color: Color
}
package game

import java.awt.Color


class Ghost1(x:Int, y:Int, gridMap: Array[Array[Spot]],cellSize: Int) extends Ghost(x, y,gridMap,cellSize) {
  

  
  this.name = "Seppo"
  
  val r = scala.util.Random
  private val pColor = Color.YELLOW
  
  def chooseDirection(firstX:Int, firstY:Int,another:Character) = {
    val availableDirections = findDirections
    currentDirection = availableDirections(r.nextInt(availableDirections.size))
  }
  
  def color = pColor
  
  
}
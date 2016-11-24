package game

import scala.collection.mutable.Buffer
import scala.math._
import java.awt.Color
import com.sun.javafx.geom.transform.BaseTransform.Degree

class Ghost2(x:Int, y:Int, gridMap: Array[Array[Spot]],cellSize: Int) extends Ghost(x, y,gridMap,cellSize) {
  
  this.name = "Teuvo"
  

  
  def chooseDirection(firstX:Int, firstY:Int,another:Character) = {
    //Checks the direction towards another character
    val direction1 = Vector(another.x - firstX, another.y - firstY)
    
    //println("x: " + this.x + " y: " + this.y)
    
    val availableDirections = findDirections

    //Goes through all the directions and chooses the one which angle to
    // the another character direction is smallest

    
    def normalize(a:Vector[Int]):Vector[Double] = {
     val lenght:Double = sqrt(pow(a(0),2)+pow(a(1),2))
     a.map( _ / lenght)
    }
    
    def dotProduct(a: Vector[Double], b: Vector[Double]) = {
      a.zip(b).map(_ match { case (x,y) => x * y}).foldRight(0.0)(_ + _)
    }

   
    //println(availableDirections.map(direction2 =>abs(acos(dotProduct(normalize(direction2),normalize(direction1))))))
    val indexOfMinAngle = availableDirections.map(jee =>
      abs(acos(dotProduct(normalize(jee),normalize(direction1))))).zipWithIndex.minBy(_._1)._2
    //println(availableDirections.map(jee =>
    //  abs(acos(dotProduct(normalize(jee),normalize(direction1))))).map(_*180))
    currentDirection = availableDirections(indexOfMinAngle)
  }
  
    private val pColor = Color.BLUE
    def color = pColor
}
package game

import scala.collection.mutable.Buffer
import scala.math._
import java.awt.Color
import com.sun.javafx.geom.transform.BaseTransform.Degree


/*
 * This Class is a general type ghost which can behave little bit different depending on
 * the constructor parameters given
 * @Param x = initial x-coordinate of the ghost
 * @Param y = initial y-coordinate of the ghost
 * @Param gridMap = GameField
 * @Param cellSize = size of one cell in the gameField
 * @Param tileForward = How many tiles infront of the target character is the ghost aiming towards
 * @Param random2On = are random moves on or not
 */
class Ghost5(x:Int, y:Int, gridMap: Array[Array[Spot]],cellSize: Int, val tilesForward:Int,
    val random2On:Boolean,val name:String) extends Ghost(x, y,gridMap,cellSize) {
  

  this.speed = 1

  val r = scala.util.Random
  var randomCounter = 0 // counter that counts turns until random turn is turned off
  val randomMoves = 5 // Amouth of moves the ghost makes random
  
  
  var randomCounter2 = 0 // counter that counts when the random move is made
  val randomActivator = 3 // number that indicates when the random move is made
  
  
  def chooseDirection(firstX:Int, firstY:Int,another:Character) = {
    val availableDirections = findDirections
    //Random moves at the beginning of the level and after that uses the artificial intelligence
    //of the ghost
    if (randomCounter < randomMoves) {
      this.currentDirection = availableDirections(r.nextInt(availableDirections.size))
      randomCounter += 1
    } else {
      
      //If random  moves are on makes a random move ever so often
      if (random2On && randomCounter2 == randomActivator) {

        this.currentDirection = availableDirections(r.nextInt(availableDirections.size))
        randomCounter2 = 0
      } else {
        
        if (random2On) randomCounter2 += 1
        
        
        //Takes the current direction of another character
        val anotherDirection = another.currentDirection
        
        val targetX = another.x + anotherDirection(0)*this.cellSize*this.tilesForward
        val targetY = another.y + anotherDirection(1)*this.cellSize*this.tilesForward
        
        //Checks the direction towards another character
        val direction1 = Vector(targetX - firstX, targetY - firstY)
    
        //Goes through all the directions and chooses the one which angle to
        // the another character direction is smallest
        
        //Vector normalization
        def normalize(a:Vector[Int]):Vector[Double] = {
         val lenght:Double = sqrt(pow(a(0),2)+pow(a(1),2))
         a.map( _ / lenght)
        }
        
        // vector dot product
        def dotProduct(a: Vector[Double], b: Vector[Double]) = {
          a.zip(b).map(_ match { case (x,y) => x * y}).foldRight(0.0)(_ + _)
        }
    
        val indexOfMinAngle = availableDirections.map(jee =>
          abs(acos(dotProduct(normalize(jee),normalize(direction1))))).zipWithIndex.minBy(_._1)._2
    
        this.currentDirection = availableDirections(indexOfMinAngle)
      }
    }
  }
  
}
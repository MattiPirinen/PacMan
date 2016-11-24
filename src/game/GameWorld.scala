package game



class GameWorld(val name: String) {
  
  var hasGameEnded:Int = 0
  var hasGameBeenLost = false
  val width = 28 // "cells" in width direction
  val height = 31 // "cells" in heigth direction
  
  val worldGrid: Array[Array[Spot]] = gameField.gridMap(1) //Map for the game
  var pointsInMap = gameField.amonthOfPoints(1) - 1 //points in the map
  
  
  val cellSize = 25
  
  
  //creates random ghosts
  val ghostRandom: Vector[Ghost] = Vector(new Ghost1(15 * this.cellSize, 15 * this.cellSize, worldGrid, cellSize),
                                      new Ghost1(15 * this.cellSize, 15 * this.cellSize,worldGrid,cellSize),
                                      new Ghost1(15 * this.cellSize, 15 * this.cellSize,worldGrid,cellSize),
                                      new Ghost2(15 * this.cellSize, 14 * this.cellSize,worldGrid,cellSize))
                                      
             
  this.ghostRandom.foreach(_.speed = 3)
  
  val player = new Player(cellSize, cellSize, worldGrid) // Game player
  var points = 0
  
  def wonGame:Boolean = pointsInMap == 0
  
  
  def movePlayer = {
    if (player.counter == player.speed) {
      
      //Remove player from previous spot
      worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).removePlayer
      
      //Moves the player
      
      var moveVector = Vector(0,0)
      var addX = 0
      var addY = 0
      
      if (player.nextDirection != None) {
        if (player.isThereNoWall(player.nextDirection.get,worldGrid,this.cellSize)){
          player.currentDirection = player.nextDirection.get
          player.nextDirection = None
          moveVector = player.currentDirection
        } 
      }
      
      if (player.isThereNoWall(player.currentDirection,worldGrid,this.cellSize)) {  
        moveVector = player.currentDirection
      }
      
      //Moves the player
      moveCharacter(player,moveVector)
      
      // if there is an item in the cell player is in removes it
      var itemi:Option [Item] = None
      if (worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).hasItem) {
        itemi = worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).removeItem
        itemi.get.itemType match {
          case "pointItem" => {
            points += 1
            this.pointsInMap -= 1
          }
        }
        if (this.pointsInMap == 0) this.hasGameEnded = 2
          
      }
      
      //Indicates to the spot where player is that the spot has player
      
      worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).addPlayer
      player.counter = 1
    } else player.counter += 1
    
  }
  
  // This method moves the character. If character goes outside of the map, it
  // appears from the other side of the map.
  def moveCharacter(character: Character, moveVector: Vector[Int]) =  {
    val addX = moveVector(0)
    val addY = moveVector(1)

    if (character.x + addX < 0) {
        character.move(this.width * this.cellSize - this.cellSize*2, character.y + addY)
      } else if (character.y + addY < 0) {
        character.move(character.x + addX, this.height * this.cellSize - this.cellSize*2)
      } else if (character.x + addX == this.width * this.cellSize - this.cellSize) {
        character.move(1, character.y + addY)
      } else if (character.y + addY == this.height * this.cellSize - this.cellSize) {
        character.move(character.x + addX, 1)
      } else {
        character.move(character.x + addX, character.y + addY)
      }
  }  
  
  
  //This methods checks if player can make the requested direction change
  //If it is not possible reservers the direction as nextDirection.
  def checkDirectionChange(inputDirection:String) = {
    if (player.directions.contains(inputDirection)) {
      if (player.isThereNoWall(player.directions(inputDirection), worldGrid, cellSize)) {
        player.currentDirection = player.directions(inputDirection)
      } else {
        player.nextDirection = Some(player.directions(inputDirection))
      }
    }
    
  }
  
  def moveGhost(ghost :Ghost) = {


    
    if (ghost.counter == ghost.speed) {
      if (ghost.findDirections.size > 0) ghost.chooseDirection(ghost.x,ghost.y,player)
      moveCharacter(ghost,ghost.currentDirection)
      if (worldGrid((ghost.x + cellSize/2) /cellSize)((ghost.y + cellSize/2) / cellSize).hasPlayer)
        this.hasGameEnded = 1
      ghost.counter = 1
    } else ghost.counter += 1
    
  }
  
}
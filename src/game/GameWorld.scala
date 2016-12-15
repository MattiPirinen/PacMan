package game

import javax.sound.sampled._

/*
 * Thic companion object contains the information about the game that goes beyod one
 * game level
 */


object GameWorld {
  var gameState = "Game"
  var currentLevel = 1
  var totalPoints = 0 // Total points gotten in the game
  var pointLossCounter = 0
  val pointLossTresshould = 100
  
  def losePoints = {
    if(pointLossCounter == pointLossTresshould) {
      totalPoints -= 1
      pointLossCounter = 0
    } else {
      pointLossCounter += 1
    }
  }
  
  def madeToLeaderboard:Boolean = {
    val leaderBoard = LeaderBoard.showLeaderBoard().map(_.split(":")(1).toInt)
    if (leaderBoard.min > totalPoints) false else true
    
  }
  
  
  
}
/*
 * This class represents one game level in the this game
 * @Param name = Name of the game-level
 * @Param currentLevel = int that tells us which gameField to use in the level
 */
class GameWorld(val name: String, currentLevel:Int) {
  
  var hasGameBeenLost = false 
  var lives = 3
  val width = 28 // "cells" in width direction
  val height = 31 // "cells" in height direction
  var pointsInMap =  0//points in the map
  val pointsFromGhost = 5 //How many points player gets when he eats a ghost
  val pointsFromItem = 1 // How many points player gets when he eats a pointItem
  
  //Sets the initial values for powerPellet item effects
  var powerPelletActive = false
  var pelletDuration = 0
  
  
  
  // #################################### Game world creation ##############################################
  val worldGrid: Array[Array[Spot]] = gameField.gridMap(currentLevel) //Map for the game
  
  //Adds items every 50th item is a PowerPellet
  val r = scala.util.Random
  var randomArvo = 0
  for (x <- worldGrid; y <- x) {
    if (y.canHaveItem) {
      randomArvo = r.nextInt(50)
      if(randomArvo == 29) {
        y.addItem(new PowerPelletItem)
        y.itemType = "powerPellet"
      } else {
        y.addItem(new pointItem2)
        y.itemType = "pointItem"
        this.pointsInMap += 1
      }
      
    }
  }
  
  //Size for game cells
  val cellSize = 25
  
  
  //Creates ghost homes
  val ghostHomes: Vector[Character] = Vector(new GhostHome(1*this.cellSize,1*this.cellSize),
                                             new GhostHome(28*this.cellSize,1*this.cellSize), 
                                             new GhostHome(1*this.cellSize,30*this.cellSize),
                                             new GhostHome(30*this.cellSize,28*this.cellSize))
  
  //creates random ghosts
  val ghostRandom: Vector[Ghost] = Vector(new Ghost5(14 * this.cellSize, 14 * this.cellSize, worldGrid, cellSize, 0, false, "Blinky"),
                                      new Ghost5(14 * this.cellSize, 14 * this.cellSize,worldGrid,cellSize,3,false,"Clyde"),
                                      new Ghost5(14 * this.cellSize, 14 * this.cellSize,worldGrid,cellSize,-3,false,"Inky"),
                                      new Ghost5(14 * this.cellSize, 14 * this.cellSize,worldGrid,cellSize,0,true,"Pinky"))
                                      
  //Speed for ghosts
  this.ghostRandom.foreach(_.speed = 1)
  
  // Creates player
  val player = new Player(cellSize, cellSize, worldGrid) // Game player
  
  // Amount of points collected
  var points = 0
  
  

  

  //Has the game been won?
  def wonGame:Boolean = pointsInMap == 0
  
  
  
  /* 
   * This method moves the player
   */
  
  def movePlayer = {
    if (player.counter == player.speed) {
      
      //resets counter
      player.counter = 1
      
      
      //Remove player from previous spot
      worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).hasPlayer = false
      
      //Moves the player
      
      var moveVector = Vector(0,0)
      var addX = 0
      var addY = 0
      
      //Chooses the move vector
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
      
      // if there is an item in the cell player is in removes
      var itemi:Option [Item] = None
      if (worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).hasItem) {
        itemi = worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).removeItem
        itemi.get.itemType match {
          case "pointItem" => {
            points += 1
            this.pointsInMap -= 1
            GameWorld.totalPoints += this.pointsFromItem
          }
          case "powerPellet" => {
            this.activatePowerPellet()
          }
        }
        if (this.pointsInMap == 0) {
          GameWorld.gameState = "Victory"
        }
          
      }
      
      //Indicates to the spot where player is that the spot has player
      
      worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).hasPlayer = true
      
      // Sets the player speed according to the spot speed
      worldGrid((player.x + cellSize/2) /cellSize)((player.y + cellSize/2) / cellSize).playerSpeed(player)
    } else player.counter += 1
    
  }
  
  // This method moves the character. If character goes outside of the map, it
  // appears from the other side of the map.
  private def moveCharacter(character: Character, moveVector: Vector[Int]) =  {
    val addX = moveVector(0)
    val addY = moveVector(1)

    if (character.x + addX < 0) {
        character.move(this.width * this.cellSize - this.cellSize*2.toInt, character.y + addY)
      } else if (character.y + addY < 0) {
        character.move(character.x + addX, this.height * this.cellSize - this.cellSize*2.toInt)
      } else if (character.x + addX == this.width * this.cellSize - this.cellSize) {
        character.move(1, character.y + addY)
      } else if (character.y + addY == this.height * this.cellSize - this.cellSize) {
        character.move(character.x + addX, 1)
      } else {
        character.move(character.x + addX, character.y + addY)
      }
  }  
  
  
  //This methods checks if player can make the requested direction change
  //If it is not possible reserves the direction as nextDirection.
  def checkDirectionChange(inputDirection:String) = {
    if (player.directions.contains(inputDirection)) {
      if (player.isThereNoWall(player.directions(inputDirection), worldGrid, cellSize)) {
        player.currentDirection = player.directions(inputDirection)
      } else {
        player.nextDirection = Some(player.directions(inputDirection))
      }
    }
    
  }
  
  
  //This methord moves the ghost
  def moveGhost(ghost :Ghost,home:Character) = {

    //Moves the ghost
    if (ghost.counter == ghost.speed) {
      val availableDirections = ghost.findDirections
      if (availableDirections.size > 1 || availableDirections.size != 0 && 
          availableDirections(0) != ghost.currentDirection) 
        if (this.powerPelletActive) {
          ghost.chooseDirection(ghost.x,ghost.y,home)
        } else {
        ghost.chooseDirection(ghost.x,ghost.y,player)
        }
      moveCharacter(ghost,ghost.currentDirection)
      
      //If there is a player where the ghost moved acts accordingly
      
      //If powerPellet is activated ghost goes back to base
      if (worldGrid((ghost.x + cellSize/2) /cellSize)((ghost.y + cellSize/2) / cellSize).hasPlayer) {
        if (this.powerPelletActive == true){
          ghost.x=14 * this.cellSize
          ghost.y=14 * this.cellSize
          ghost.counter = -1000
          Sound.playPowerupSound()
          GameWorld.totalPoints += this.pointsFromGhost
          }
        //If not player dies
        else {
          Sound.playDeathSound()
          if (lives == 1) {
            GameWorld.gameState = "Death"
            }
          else {
            player.counter = -600
            ghostRandom.foreach(i => i.counter = -600)
            player.x = 1 * this.cellSize
            player.y = 1 * this.cellSize
            ghostRandom foreach(i => i.x = 14 * this.cellSize)
            ghostRandom foreach(i => i.y = 14 * this.cellSize)
            lives -=1
          }
          }
      }
      else ghost.counter = 1
    } else ghost.counter += 1
    
    
    //Sets the speed of the ghost according to the speed in the spot
    worldGrid((ghost.x + cellSize/2) /cellSize)((ghost.y + cellSize/2) / cellSize).ghostSpeed(ghost)
  }
  
  //Activates powerpellet
  def activatePowerPellet(){
    this.powerPelletActive = true
    this.pelletDuration = View.framerate * 170
    Sound.playPowerPillSound() }
  
  
}
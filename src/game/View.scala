package game

import scala.swing._
import scala.swing.event._
import java.awt.Color
import javax.swing._
import javax.imageio.ImageIO
import java.io.File

object View extends SimpleSwingApplication {


  var world = new GameWorld("Peli1", GameWorld.currentLevel)
  GameWorld.gameState = "StartScreen"
  
  
  //Variables to be used in player animation looping
  var playerAnimator1 = 0
  var playerAnimator2 = 0
  var animatorCounter = 0
  val animatorCounterCap = 8
  
  val screenWidth = world.width * world.cellSize
  val screenHeight = world.height * world.cellSize
  val cellSize = world.cellSize
  val framerate: Int = 1000 / 150 //Refresh rate of the game
  val r = scala.util.Random

  // #################################### Creation of GUI objects ##############################################

  //Play Area
  val canvas: GridPanel = new GridPanel(rows0 = world.height, cols0 = world.width) {
    preferredSize = new Dimension(screenWidth, world.height * cellSize) // how many pixels the play window is

    override def paintComponent(g: Graphics2D) {

      GameWorld.gameState match {

        
        case "Game" => {
          
          //Draw the background
          for (i <- 0 until world.width) {
            for (k <- 0 until world.height) { // Loop through the world grid
              g.drawImage(world.worldGrid(i)(k).image, i*cellSize, k*cellSize, null)  //Draw images that is defined in spot
              if (world.worldGrid(i)(k).hasItem) {
                g.setColor(Color.BLUE)
                if (world.worldGrid(i)(k).itemType == "pointItem") {
                  g.fillOval(i * cellSize + cellSize / 3, k * cellSize + cellSize / 3, cellSize / 3, cellSize / 3)
                } else if (world.worldGrid(i)(k).itemType == "powerPellet") {
                  g.setColor(Color.WHITE)
                  g.fillOval(i * cellSize + cellSize / 4, k * cellSize + cellSize / 4, cellSize / 2, cellSize / 2)
                }

              }
            }
          }
          //Draw player
          g.drawImage(Graphics.pacman(world.player.currentDirection), 
              world.player.x, world.player.y, 
              world.player.x+ world.cellSize, world.player.y + world.cellSize,
              world.cellSize * playerAnimator1, world.cellSize * playerAnimator2, 
              world.cellSize * playerAnimator1 + world.cellSize, world.cellSize * playerAnimator2 + world.cellSize,
              null)

          //Draw ghosts
          for (ghost <- world.ghostRandom) {
            if (world.powerPelletActive)
              g.drawImage(Graphics.scared(ghost.currentDirection), ghost.x, ghost.y, null)
            else if (ghost.name == "Blinky")
              g.drawImage(Graphics.blinky(ghost.currentDirection), ghost.x, ghost.y, null)
            else if (ghost.name == "Pinky")
              g.drawImage(Graphics.pinky(ghost.currentDirection), ghost.x, ghost.y, null)
            else if (ghost.name == "Inky")
              g.drawImage(Graphics.inky(ghost.currentDirection), ghost.x, ghost.y, null)
            else if (ghost.name == "Clyde")
              g.drawImage(Graphics.clyde(ghost.currentDirection), ghost.x, ghost.y, null)
          }
        }

        case "Death" => {
          g.setColor(Color.BLACK)
          g.fillRect(0, 0, screenWidth * cellSize, screenHeight * cellSize)
        }

        case "Victory" => {
          g.setColor(Color.BLACK)
          g.fillRect(0, 0, screenWidth * cellSize, screenHeight * cellSize)
        }

        case "StartScreen" => {
          g.drawImage(ImageIO.read(new File("pictures/StartScreen.png")), null, 0, 0)
        }
        case "PauseScreen" => {
          g.drawImage(ImageIO.read(new File("pictures/PauseScreen.png")), null, 0, 0)
        }
      }
    }

  }

  //Label where points are displayed
  val pointCalculator = new Label("")
  val labelFont = new Font("Arial", 0, 36)
  pointCalculator.font = labelFont

  //Label where the lives are displayed
  val lifeCalculator = new Label("")
  lifeCalculator.font = labelFont

  //Buttons
  val buttonSize = new Dimension(world.width * world.cellSize / 4 - 5, 50)
  val buttonFont = new Font("Arial", 0, 20)

  val newGameButton = new Button("New Game") {

    listenTo(this)
    reactions += {
      case clickEvent: ButtonClicked =>
        GameWorld.currentLevel = 1
        GameWorld.totalPoints = 0
        world = new GameWorld("Peli1", GameWorld.currentLevel)
        GameWorld.gameState = "Game"
        canvas.requestFocus()

    }

    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }

  val quitButton = new Button("Quit") {

    listenTo(this)
    reactions += {
      case clickEvent: ButtonClicked =>
        quit()
    }

    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
  val helpButton = new Button("Help") {

    listenTo(this)
    reactions += {
      case clickEvent: ButtonClicked =>
        GameWorld.gameState = "PauseScreen"
        world.player.pauseMove()
        world.ghostRandom.foreach(_.pauseMove())
        
        canvas.requestFocus()
    }

    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
  val leaderBoardButton = new Button("LeaderBoard") {

    listenTo(this)
    reactions += {
      case clickEvent: ButtonClicked =>
        showLeaderBoard
        
        canvas.requestFocus()
    }

    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
  
  //Panel for labels
  val horizontalPanelLabels = new BoxPanel(Orientation.Horizontal)
  horizontalPanelLabels.contents += lifeCalculator
  horizontalPanelLabels.contents += pointCalculator

  
  //Panel for buttons
  val horizontalPanelButtons = new BoxPanel(Orientation.Vertical) {
    contents += newGameButton
    contents += quitButton
    contents += helpButton
    contents += leaderBoardButton
  }

  //Panel for all the elements
  val verticalPanel = new BorderPanel {
    layout += canvas -> BorderPanel.Position.Center
    layout += horizontalPanelLabels -> BorderPanel.Position.South
    layout += horizontalPanelButtons -> BorderPanel.Position.East
  }

  // ################################################################################################

  //#################################### Creation of the MainFrame ##############################################

  def top = new MainFrame {
    title = "Pac-Man Rip Off"
    val windowSize = new Dimension(screenWidth+200, screenHeight + 100)
    preferredSize = windowSize
    maximumSize  = windowSize
    minimumSize = windowSize
    contents = verticalPanel

    canvas.requestFocus()

    //Creates a timer which will run with the framerate
    val timer = new Timer(framerate, Swing.ActionListener { e =>
      
      //Player animation changes image ever so often
      if (animatorCounter == animatorCounterCap) {
        animatorCounterLooper // Loops the two values which chooses which one of the pictures to show
        animatorCounter = 0
      } else animatorCounter += 1
      

      //If the game is going on
      if (GameWorld.gameState == "Game") {
        
        //Loses points when time goes on
        GameWorld.losePoints
        //Moves the characters
        world.movePlayer
        for (i <- world.ghostRandom.indices) {
          world.moveGhost(world.ghostRandom(i),world.ghostHomes(i))
        }
        
        //Displays current points and lives
        pointCalculator.text = "Points: " + GameWorld.totalPoints.toString()
        lifeCalculator.text = "Lives left: " + world.lives + "      "
        
        
        if (world.powerPelletActive == true) {
          if (world.pelletDuration > 0) {
            world.pelletDuration -= 1
          } else {
            world.powerPelletActive = false
            if (Sound.pPillSound.isRunning()) Sound.pPillSound.stop()
          }
        }
        
        
        
      } 
      
      //If player has won
      else if (GameWorld.gameState == "Victory") {
        //If player is at the last level ends the game
        if (GameWorld.currentLevel == 3) {
          pointCalculator.text = "YOU WON! CONGRATULATIONS!"
          if (GameWorld.madeToLeaderboard) {
            saveToLeaderBoard
          }
            showLeaderBoard
            returnToStartScreen
        }
        //Else moves player to the next game
        else {
          GameWorld.currentLevel += 1
          world = new GameWorld("Peli1", GameWorld.currentLevel)
          GameWorld.gameState = "Game"
          world.player.counter = -200
          world.ghostRandom.foreach(_.counter = -200)
        }
      } 
      
      //If player has died
      else if (GameWorld.gameState == "Death") {
        pointCalculator.text = "GAME OVER"
        if (GameWorld.madeToLeaderboard) {
          saveToLeaderBoard
        }
          showLeaderBoard
          returnToStartScreen
      }

      repaint()
      
    })

    timer.start()

    // Listens to the keys and acts according to the input
    listenTo(canvas.keys)

    reactions += {

      case e: KeyPressed =>
        e.key match {
          case Key.Up =>
            world.checkDirectionChange("Up")
          case Key.Down =>
            world.checkDirectionChange("Down")
          case Key.Left =>
            world.checkDirectionChange("Left")
          case Key.Right =>
            world.checkDirectionChange("Right")
          case Key.P =>
            GameWorld.gameState = "PauseScreen"
            world.player.pauseMove()
            world.ghostRandom.foreach(_.pauseMove())
          case Key.R =>
            GameWorld.gameState = "Game"
            world.player.resumeMove()
            world.ghostRandom.foreach(_.resumeMove())
          case _ =>

        }
    }
  }
  
  
  //Saves the given name and score to leaderboard
  def saveToLeaderBoard = {
    
    val nimi = Dialog.showInput(canvas, "Concratulations!\nYou made it to the Leaderboard!\n" +
      "Please give your name below.","",icon=null, initial="").get
    val tulos = nimi+":"+ GameWorld.totalPoints
    LeaderBoard.addScore(tulos)
    LeaderBoard.saveScores()

  }
  
  //Shows leaderboard in a dialog
  def showLeaderBoard = {
    val leaderBoard = LeaderBoard.showLeaderBoard()
    Dialog.showMessage(canvas, leaderBoard.mkString("\n"),"Best of the Best:")
  }
  
  //Returns to startscreen and sets default values to the labels
  def returnToStartScreen = {
    GameWorld.gameState = "StartScreen"
    pointCalculator.text = ""
    lifeCalculator.text = ""
  }
  
  
  //Loops through animator looper variables
  def animatorCounterLooper = {
    if (playerAnimator1 == 0 && playerAnimator2 == 0) {
      playerAnimator1 = 1
    } else if (playerAnimator1 == 1 && playerAnimator2 == 0) {
      playerAnimator1 = 0
      playerAnimator2 = 1
    } else if (playerAnimator1 == 0 && playerAnimator2 == 1) {
      playerAnimator1 = 1
    } else {
      playerAnimator1 = 0
      playerAnimator2 = 0
    }
    
  }
  
}

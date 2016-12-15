package game

import scala.swing._
import scala.swing.event._
import java.awt.Color
import javax.swing._
import javax.imageio.ImageIO
import java.io.File

object View extends SimpleSwingApplication {

  var currentLevel = 1
  var world = new GameWorld("Peli1", currentLevel)
  GameWorld.gameState = "StartScreen"

  var showHelpMessage = false
  val screenWidth = world.width * world.cellSize
  val screenHeight = world.height * world.cellSize
  val cellSize = world.cellSize
  val framerate: Int = 1000 / 150 //Refresh rate of the game
  var counter = 1
  val r = scala.util.Random

  // #################################### Creation of GUI objects ##############################################

  val canvas: GridPanel = new GridPanel(rows0 = world.height, cols0 = world.width) {
    preferredSize = new Dimension(screenWidth, world.height * cellSize) // how many pixels the play window is

    override def paintComponent(g: Graphics2D) {

      GameWorld.gameState match {

        case "Game" => {
          for (i <- 0 until world.width) {
            for (k <- 0 until world.height) { // Loop through the world grid
              world.worldGrid(i)(k).color match { // Match what is found in every position
                case "BLACK" => { // If a wall is there, change color to black and paint a black tile representing a wall
                  g.setColor(Color.BLACK)
                  g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
                }
                case "CYAN" => { // If a floor is there, change color to cyan and paint a cyan tile representing floor
                  g.setColor(Color.CYAN)
                  g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
                }
              }
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
          g.drawImage(Graphics.pacman(world.player.currentDirection), world.player.x, world.player.y, null)

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

            // g.setColor(ghost.color)
            // g.fillOval(ghost.x, ghost.y, cellSize, cellSize)
          }

          g.setColor(Color.GRAY)
          g.setColor(Color.RED)
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
          g.drawImage(ImageIO.read(new File("StartScreen.png")), null, 0, 0)
        }
      }

      // Creates red lines at each side of the player done for development purposes
      /*
      g.fillRect(player.x,0,1,height*cellSize)
      g.fillRect(player.x+cellSize,0,1,height*cellSize)
      g.fillRect(0,player.y,width*cellSize,1)
      g.fillRect(0,player.y+cellSize,width*cellSize,1)
      */
    }

  }

  //Label where points are displayed
  val pointCalculator = new Label("")
  val labelFont = new Font("Arial", 0, 36)
  pointCalculator.font = labelFont
  //pointCalculator.horizontalAlignment = Alignment.Center

  val lifeCalculator = new Label("Lives left: " + world.lives + "      ")
  lifeCalculator.font = labelFont

  val buttonSize = new Dimension(world.width * world.cellSize / 4 - 5, 50)
  val buttonFont = new Font("Arial", 0, 20)

  val newGameButton = new Button("New Game") {

    listenTo(this)
    reactions += {
      case clickEvent: ButtonClicked =>
        world = new GameWorld("Peli1", currentLevel)
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
        GameWorld.gameState = "StartScreen"
        canvas.requestFocus()
    }

    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
  val optionButton = new Button("Option") {

    listenTo(this)
    reactions += {
      case clickEvent: ButtonClicked =>
        canvas.requestFocus()
      // TODO: This is missing!
    }

    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }

  val horizontalPanelLabels = new BoxPanel(Orientation.Horizontal)
  horizontalPanelLabels.contents += lifeCalculator
  horizontalPanelLabels.contents += pointCalculator

  val horizontalPanelButtons = new BoxPanel(Orientation.Horizontal) {
    contents += newGameButton
    contents += quitButton
    contents += helpButton
    contents += optionButton
  }

  val verticalPanel = new BorderPanel {
    layout += canvas -> BorderPanel.Position.North
    layout += horizontalPanelLabels -> BorderPanel.Position.Center
    layout += horizontalPanelButtons -> BorderPanel.Position.South
  }

  // ################################################################################################

  //#################################### Creation of the MainFrame ##############################################

  def top = new MainFrame {
    title = "Pac-Man Rip Off"
    preferredSize = new Dimension(screenWidth, screenHeight + 150)

    contents = verticalPanel

    canvas.requestFocus()

    //Creates a timer which will run with the framerate
    val timer = new Timer(framerate, Swing.ActionListener { e =>
      world.movePlayer
      for (ghost <- world.ghostRandom) {
        world.moveGhost(ghost)
      }
      repaint()
      if (GameWorld.gameState == "Game") {
        pointCalculator.text = "Points left: " + world.pointsInMap.toString()
        lifeCalculator.text = "Lives left: " + world.lives + "      "
      } else if (GameWorld.gameState == "Victory") {
        if (currentLevel == 3) {
          pointCalculator.text = "YOU WON! CONGRATULATIONS!"
        } else {
          currentLevel += 1
          world = new GameWorld("Peli1", currentLevel)
        }
      } else if (GameWorld.gameState == "Death") {
        var stopped = 0
        if (stopped == 0) {
          world.ghostRandom.foreach(i => i.pauseMove())
          pointCalculator.text = "GAME OVER"
          stopped = 1
        }
      }

      if (world.powerPelletActive == true) {
        if (world.pelletDuration > 0) {
          world.pelletDuration -= 1
        } else {
          world.powerPelletActive = false
          if (Sound.pPillSound.isRunning()) Sound.pPillSound.stop()
        }
      }
    })

    timer.start()

    // Listens to the keys and acts according to the input
    listenTo(canvas.keys)

    reactions += {

      //      case KeyPressed(_, c,_,_) => {
      //        world.checkDirectionChange(c.toString())
      //
      //      }
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
          case _ =>

        }
    }
  }
}

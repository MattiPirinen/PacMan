package game

import scala.swing._
import scala.swing.event._
import java.awt.Color
import javax.swing._

object View extends SimpleSwingApplication {
  
  val world = new GameWorld("Peli1")
 
  val screenWidth = world.width * world.cellSize
  val screenHeight = world.height * world.cellSize
  val cellSize = world.cellSize
  val framerate: Int = 1000/150 //Refresh rate of the game
  var counter = 1
  val r = scala.util.Random 
  
  
   // #################################### Creation of GUI objects ##############################################
  
  
  val canvas: GridPanel = new GridPanel(rows0 = world.height, cols0 = world.width) { 
    preferredSize = new Dimension(screenWidth, world.height * cellSize) // how many pixels the play window is
  
    
    override def paintComponent(g: Graphics2D) {
      
      
      world.hasGameEnded match {
        case 0 => {
          for (i <- 0 until world.width) {
            for (k <- 0 until world.height) { // Loop through the world grid
              world.worldGrid(i)(k).color match {       // Match what is found in every position
                case "BLACK" => {          // If a wall is there, change color to black and paint a black tile representing a wall
                  g.setColor(Color.BLACK)
                  g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
                }
                case "CYAN" => {         // If a floor is there, change color to cyan and paint a cyan tile representing floor
                  g.setColor(Color.CYAN)
                  g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
                }
              }
              if (world.worldGrid(i)(k).hasItem) {
                g.setColor(Color.BLUE)
                if (world.worldGrid(i)(k).itemType == "pointItem")  {
                g.fillOval(i * cellSize+cellSize/3, k * cellSize+cellSize/3, cellSize/3, cellSize/3)
                }
                else if (world.worldGrid(i)(k).itemType == "powerPellet") {
                g.setColor(Color.WHITE)  
                g.fillOval(i * cellSize+cellSize/4, k * cellSize+cellSize/4, cellSize/2, cellSize/2) 
                }
                
              }
            }
          }
          g.setColor(Color.RED)      // Set color for the player to be drawn
          g.fillOval(world.player.x, world.player.y, cellSize, cellSize) // Draw player to its location

          for (ghost <- world.ghostRandom) {
            g.setColor(ghost.color)
            g.fillOval(ghost.x, ghost.y, cellSize, cellSize)
          }
          
          g.setColor(Color.GRAY)
          g.setColor(Color.RED)
        }
        
        case 1 => {
          g.setColor(Color.BLACK)
          g.fillRect(0,0, screenWidth * cellSize, screenHeight * cellSize)   
        }
        
        case 2 => {
          g.setColor(Color.BLACK)
          g.fillRect(0,0, screenWidth * cellSize, screenHeight * cellSize)   
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

  
  //Button where points are displayed
  val pointCalculator = new Label("")
  pointCalculator.font = new Font("Arial",0,36)
  pointCalculator.horizontalAlignment = Alignment.Right
  
  
  
  val buttonSize = new Dimension(world.width*world.cellSize/4-5,50)
  val buttonFont = new Font("Arial",0,20)
  
  val newGameButton = new Button("New Game") {
    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
 
  val quitButton = new Button("Quit") {
    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
  val helpButton = new Button("Help") {
    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
  val optionButton = new Button ("Option") {
    preferredSize = buttonSize
    minimumSize = buttonSize
    maximumSize = buttonSize
    font = buttonFont
  }
  
  
  
  val horizontalPanel = new BoxPanel(Orientation.Horizontal)
  
  horizontalPanel.contents += newGameButton
  horizontalPanel.contents += quitButton
  horizontalPanel.contents += helpButton
  horizontalPanel.contents += optionButton
  
  val verticalPanel = new BoxPanel(Orientation.Vertical)
  
  verticalPanel.contents += canvas
  verticalPanel.contents += pointCalculator
  verticalPanel.contents += horizontalPanel
  
   // ################################################################################################
  
  
   //#################################### Creation of the MainFrame ##############################################
  
  def top = new MainFrame {
    title = "Pac-Man Rip Off"
    preferredSize = new Dimension(screenWidth, screenHeight+100)

    contents = verticalPanel

    canvas.requestFocus()
    
    //Creates a timer which will run with the framerate
    val timer = new Timer(framerate,Swing.ActionListener { e => 
        world.movePlayer
        for (ghost <- world.ghostRandom) {
          world.moveGhost(ghost)
        }
        repaint()  
        if (world.hasGameEnded == 0) {
          pointCalculator.text = "Points left: " + world.pointsInMap.toString()
        } else if (world.hasGameEnded == 2) {
          pointCalculator.text = "YOU WON! CONGRATULATIONS!"
        } else if(world.hasGameEnded == 1) {
          pointCalculator.text = "GAME OVER"
        }
        
        
        if (world.powerPelletActive == true) {
          if (world.pelletDuration > 0) {
            world.pelletDuration -= 1
            }
          else {
            world.powerPelletActive = false
            if (Sound.pPillSound.isRunning()) Sound.pPillSound.stop()
          }
        }
    })
    
    timer.start()
            
    // Listens to the keys and acts according to the input
    listenTo(canvas.keys)
    reactions += {
      
      case KeyPressed(_, c,_,_) => {
        world.checkDirectionChange(c.toString())

        
      }
    }
  }
}

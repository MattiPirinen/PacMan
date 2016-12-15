package game

import scala.swing._
import scala.swing.event._
import java.awt.Color
import javax.swing._
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage

object Graphics {
  val blinkyUp = ImageIO.read(new File("pictures/blinky_up.png")) // load graphics
  val blinkyRight = ImageIO.read(new File("pictures/blinky_right.png"))
  val blinkyDown = ImageIO.read(new File("pictures/blinky_down.png"))
  val blinkyLeft = ImageIO.read(new File("pictures/blinky_left.png"))
  val clydeUp = ImageIO.read(new File("pictures/clyde_up.png"))
  val clydeRight = ImageIO.read(new File("pictures/clyde_right.png"))
  val clydeDown = ImageIO.read(new File("pictures/clyde_down.png"))
  val clydeLeft = ImageIO.read(new File("pictures/clyde_left.png"))
  val inkyUp = ImageIO.read(new File("pictures/inky_up.png"))
  val inkyRight = ImageIO.read(new File("pictures/inky_right.png"))
  val inkyDown = ImageIO.read(new File("pictures/inky_down.png"))
  val inkyLeft = ImageIO.read(new File("pictures/inky_left.png"))
  val pinkyUp = ImageIO.read(new File("pictures/pinky_up.png"))
  val pinkyRight = ImageIO.read(new File("pictures/pinky_right.png"))
  val pinkyDown = ImageIO.read(new File("pictures/pinky_down.png"))
  val pinkyLeft = ImageIO.read(new File("pictures/pinky_left.png"))
  val pacmanUp = ImageIO.read(new File("pictures/pacman_up.png"))
  val pacmanRight = ImageIO.read(new File("pictures/pacman_right.png"))
  val pacmanDown = ImageIO.read(new File("pictures/pacman_down.png"))
  val pacmanLeft = ImageIO.read(new File("pictures/pacman_left.png"))
  val scaredUp = ImageIO.read(new File("pictures/scared_up.png"))
  val scaredRight = ImageIO.read(new File("pictures/scared_right.png"))
  val scaredDown = ImageIO.read(new File("pictures/scared_down.png"))
  val scaredLeft = ImageIO.read(new File("pictures/scared_left.png"))
  val floor = ImageIO.read(new File("pictures/floor.png"))
  val grass = ImageIO.read(new File("pictures/grass.png"))
  val fence = ImageIO.read(new File("pictures/fence.png"))
  val playerSlowFloor = ImageIO.read(new File("pictures/player_slow_floor.png"))
  val ghostSlowFloor = ImageIO.read(new File("pictures/ghost_slow_floor.png"))

  def blinky(dir: Vector[Int]): Image = { // check the direction of ghosts/player and return matching pic
    if (dir == Vector(0, -1)) blinkyUp
    else if (dir == Vector(1, 0)) blinkyRight
    else if (dir == Vector(0, 1)) blinkyDown
    else blinkyLeft
  }

  def clyde(dir: Vector[Int]): Image = {
    if (dir == Vector(0, -1)) clydeUp
    else if (dir == Vector(1, 0)) clydeRight
    else if (dir == Vector(0, 1)) clydeDown
    else clydeLeft
  }

  def inky(dir: Vector[Int]): Image = {
    if (dir == Vector(0, -1)) inkyUp
    else if (dir == Vector(1, 0)) inkyRight
    else if (dir == Vector(0, 1)) inkyDown
    else inkyLeft
  }

  def pinky(dir: Vector[Int]): Image = {
    if (dir == Vector(0, -1)) pinkyUp
    else if (dir == Vector(1, 0)) pinkyRight
    else if (dir == Vector(0, 1)) pinkyDown
    else pinkyLeft
  }

  def pacman(dir: Vector[Int]): Image = {
    if (dir == Vector(0, -1)) pacmanUp
    else if (dir == Vector(1, 0)) pacmanRight
    else if (dir == Vector(0, 1)) pacmanDown
    else pacmanLeft
  }

  def scared(dir: Vector[Int]): Image = {
    if (dir == Vector(0, -1)) scaredUp
    else if (dir == Vector(1, 0)) scaredRight
    else if (dir == Vector(0, 1)) scaredDown
    else scaredLeft
  }

}
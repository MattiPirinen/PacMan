package game

import java.awt.image.BufferedImage

/*
 * A abstract class that represents one grid-cell in the game field
 */
abstract class Spot {
  
  private var itemi: Option[Item] = None
  
  var canHaveItem:Boolean
  var hasPlayer:Boolean //Is player on top of the cell
  var isUnPassable: Boolean // Can this cell be walked through
  var itemType = "" //A variable that tells which type of is in the cell
  val image: BufferedImage //variable that the cell graphics are saved to

  def hasItem = itemi != None //Does the cell have item in it?
  
  def addItem(item:Item): Unit = this.itemi = Some(item) // adds an item to the cell
  
  
  //Removes the item from the cell
  def removeItem: Option[Item] = {
    var valiItemi: Option[Item] = None
    if (this.itemi != None) valiItemi = this.itemi
    itemi = None
    valiItemi
  }
  
  //Sets player speed to baseSpeed
  def playerSpeed(player:Player):Unit = player.setSpeedToBase()
  
  //Sets ghost speed to baseSpeed
  def ghostSpeed(ghost:Ghost):Unit = ghost.setSpeedToBase()
  
}





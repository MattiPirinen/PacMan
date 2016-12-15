package game

/*
 * A collectable item which gives points to the player
 */

class pointItem2 extends Item {
  private val pItemType = "pointItem"
  
  def itemType = pItemType
  
  val pointsFromItem = 5
}
package game

trait Spot {
  
  def unPassable: Boolean
  
  def color: String
  
  def hasItem: Boolean
  
  def itemType: String
  
  def removeItem: Option[Item]
  
  def hasPlayer: Boolean
  
  def addPlayer: Unit
  
  def removePlayer: Unit
}





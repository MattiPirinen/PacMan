package game

abstract class Spot {
  
  private var itemi: Option[Item] = None
  
  var canHaveItem:Boolean
  var hasPlayer:Boolean
  var isUnPassable: Boolean
  var color:String
  var itemType = ""
  
  def hasItem = itemi != None
  
  def addItem(item:Item): Unit = this.itemi = Some(item)
  
  def removeItem: Option[Item] = {
    var valiItemi: Option[Item] = None
    if (this.itemi != None) valiItemi = this.itemi
    itemi = None
    valiItemi
  }
  
  def playerSpeed(player:Player):Unit = player.setSpeedToBase()
  
  def ghostSpeed(ghost:Ghost):Unit = ghost.setSpeedToBase()
  
}





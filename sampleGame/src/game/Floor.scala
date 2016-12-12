package game

class Floor extends Spot {
  
  private val pUnPassable = false
  private var itemi: Option[Item] = Some(new pointItem)
  private var pHasPlayer = false
  
  def hasItem = itemi != None
  
  
  def addItem(item:Item): Unit = this.itemi = Some(item)
  
  def itemType = "pointItem"
  
  def removeItem: Option[Item] = {
    var valiItemi: Option[Item] = None
    if (this.itemi != None) valiItemi = this.itemi
    itemi = None
    valiItemi
  }
  
  def unPassable: Boolean = this.pUnPassable
  
  def color = "CYAN"
  
  def hasPlayer = this.pHasPlayer
  
  def addPlayer = this.pHasPlayer = true
  
  def removePlayer = this.pHasPlayer = false
  
}

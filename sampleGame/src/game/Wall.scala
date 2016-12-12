package game

class Wall extends Spot {
  
   private val pUnPassable = true
   
   def unPassable:Boolean = this.pUnPassable
  
   def color = "BLACK"
   
   def hasItem = false
   
   def itemType = "None"
   
   def removeItem = None
   
   def hasPlayer = false
   
   def addPlayer = {}
   def removePlayer = {}
 }

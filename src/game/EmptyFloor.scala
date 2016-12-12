package game

class EmptyFloor extends Spot {
  
   private val pUnPassable = false
   
   def unPassable:Boolean = this.pUnPassable
  
   def color = "CYAN"
   
   def hasItem = false
   
   def itemType = "None"
     
   def removeItem = None
   
   def hasPlayer = false
   
   def addPlayer = {}
   def removePlayer = {}
 }

package game

case class TimerEvent (source: AnyRef) extends swing.event.Event
 
class ScalaTimer(delayTime: Int) extends swing.Publisher {
  outer =>
 
  private val t = new javax.swing.Timer(delayTime, new java.awt.event.ActionListener {
    def actionPerformed(e: java.awt.event.ActionEvent) {
      publish(new TimerEvent(outer))
    }
  })
 
  def addActionListener(listener: java.awt.event.ActionListener) {t.addActionListener(listener)}
  def fireActionPerformed(e: swing.event.ActionEvent) {publish(e)}
  def actionCommand: String = t.getActionCommand
  def actionListeners: Array[ java.awt.event.ActionListener ] = t.getActionListeners
  def delay: Int = t.getDelay
  def initialDelay = t.getInitialDelay
  def listeners[A <: java.util.EventListener](listenerType: Class[A]) = t.getListeners(listenerType)
  def coalesce: Boolean = t.isCoalesce
  def repeats: Boolean = t.isRepeats
  def running: Boolean = t.isRunning
  def removeActionListener(listener: java.awt.event.ActionListener) {t.removeActionListener(listener)}
  def restart() {t.restart()}
  def actionCommand_=(command: String) {t.setActionCommand(command)}
  def coalesce_=(flag: Boolean) {t.setCoalesce(flag)}
  def delay_=(delay: Int) {t.setDelay(delay)}
  def initialDelay_=(initialDelay: Int) {t.setInitialDelay(initialDelay)}
  def repeats_=(flag: Boolean) {t.setRepeats(flag)}
  def start() {t.start()}
  def stop() {t.stop()}
}
 
 
object ScalaTimer {
  import javax.swing.Timer
  def getLogTimers: Boolean = Timer.getLogTimers
  def setLogTimers(flag: Boolean) {Timer.setLogTimers(flag)}
}
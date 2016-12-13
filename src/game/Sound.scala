package game

import javax.sound.sampled._
import java.io.File

class Sound {
  
  
  private val pPill = new File("sounds/pPill.wav")
  var audioIn = AudioSystem.getAudioInputStream(pPill);
  val pPillSound = AudioSystem.getClip()
  pPillSound.open(audioIn)
  
  private val death = new File("sounds/death.wav")
  audioIn = AudioSystem.getAudioInputStream(death);
  val deathSound = AudioSystem.getClip()
  deathSound.open(audioIn)
  
  private val powerUp = new File("sounds/powerup.wav")
  audioIn = AudioSystem.getAudioInputStream(powerUp);
  val powerupSound = AudioSystem.getClip()
  powerupSound.open(audioIn)
  
 

  
  def playPowerPillSound() = {
      if (!pPillSound.isRunning()) {
        pPillSound.setMicrosecondPosition(0)
        pPillSound.start()   
        pPillSound.loop(Clip.LOOP_CONTINUOUSLY)
        }
    }
  
  def playDeathSound() = {
    if (!deathSound.isRunning()) deathSound.start()
      deathSound.setMicrosecondPosition(0)
    }


  def playPowerupSound() = {
     if (!powerupSound.isRunning()) {
       powerupSound.start()
       powerupSound.setMicrosecondPosition(0)
       }
      
  }

}


package org.swot

import scala.actors.TIMEOUT
import scala.actors.Actor
import scala.actors.Actor._

class TimerAction
case object StopTimer extends TimerAction
case object Tick extends TimerAction

class Timer(actor: Actor, interval: Long) extends Actor {
  var running = false
  def act() {
    while (true) {
      receiveWithin(interval) {

        case StopTimer =>
          exit()

        case TIMEOUT =>
          actor ! Tick
      }
    }
  }
}

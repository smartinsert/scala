import MusicController.{Play, Stop}
import MusicPlayer.{StartMusic, StopMusic}
import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by Ankit on 30-12-2016.
  */

object MusicController {
  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg

  def props = Props[MusicController]

}

class MusicController extends Actor {

  override def receive = {
    case Play =>
      println("Music Started ....")
    case Stop =>
      println("Music Stopped ....")
  }
}

object MusicPlayer {
  sealed trait  PlayMsg
  case object StopMusic extends PlayMsg
  case object StartMusic extends PlayMsg

}

class MusicPlayer extends Actor {

  override def receive = {
    case StartMusic =>
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! Play

    case StopMusic =>
      println("I dont want to stop music ")

    case _ =>
      println("Unknown")
  }
}

object Creation extends App {
  val system = ActorSystem("creation")

  val player = system.actorOf(Props[MusicPlayer], "player")

  player ! StartMusic

  system.terminate()
}

import akka.actor.{Actor, ActorSystem, Props}


case class WhoToGreet(Who: String)

class Greeter extends Actor {
  def receive = {
    case WhoToGreet(who) => println(s"Hello $who")
  }
}

object HelloAkkaScala extends App {

  // an actor needs an ActorSystem
  val system = ActorSystem("Hello-Akka")

  // create the greeter actor
  val greeter = system.actorOf(Props[Greeter], name = "greeter")

  // send the actor two messages
  greeter ! WhoToGreet("Akka")

  system.terminate()
}

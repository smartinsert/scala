import Checker.{BlackUser, CheckUser, WhiteUser}
import Recorder.NewUser
import Storage.AddUser
import akka.actor.{Actor, ActorRef}
import akka.util.Timeout

/**
  * Created by Ankit on 30-12-2016.
  */

case class User(username: String, email: String)

object Recorder {
  sealed trait RecorderMsg;
  case class NewUser(user: User) extends RecorderMsg

}

object Checker {
  sealed trait CheckerMsg

  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckerResponse
  case class BlackUser(user: User) extends CheckerResponse
  case class WhiteUser(user: User) extends CheckerResponse
}

object Storage {
  sealed trait StorageMsg
  case class AddUser(user: User) extends StorageMsg
}

class Storage extends Actor {
  var users = List.empty[User]

  override def receive = {
    case AddUser(user) =>
      println(s"Storage: $user added")
      users = user :: users
  }
}

class Checker extends Actor {
  val blackList = List(
    User("Adam", "adam@gmail.com")
  )

  override def receive = {
    case CheckUser(user) if blackList.contains(user) =>
      println(s"Checker: $user is blacklisted.")
      sender() ! BlackUser(user)

    case CheckUser(user) =>
      println(s"Checker: $user is not blacklisted.")
      sender ! WhiteUser(user)
  }
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case NewUser(user) =>
      checker ? CheckUser(user) map {
        case WhiteUser(user) =>
          storage ! AddUser(user)
        case BlackUser(user) =>
          println(s"Recorder: $user in the blacklist")
      }
  }

}
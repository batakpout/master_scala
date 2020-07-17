package teachingchotu

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object Actor1 extends App {

  // actor assembles messages in mailbox
  class BankAccount extends Actor  {

    override def receive: Receive = {
      case "Hello" => println("I received hello mesage")
    }

  }

  val actorSystem = ActorSystem("BankASsustem")

  val actor1: ActorRef = actorSystem.actorOf(Props[BankAccount], "BankAccoutnActor1")
  println(actor1)
  println(actor1.path)
  println(actor1.path.name)

  actor1 ! "Hello" // tell
  //actor1.tell("Hello", null)

}
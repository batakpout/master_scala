package akka.actors.part1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object CommunicatingActorsExample1 extends App {

  case class StartCounting(n: Int, other: ActorRef)

  case class CountDown(n: Int)

  class CountDownActor extends Actor {

    override def receive: PartialFunction[Any, Unit] = {
      case StartCounting(n, other) =>
        println(s"From StartCounting Case -> $n and self $self")
        other ! CountDown(n - 1)
      case CountDown(n) =>
        if (n > 0) {
          println(s"From CountDown Case -> $n  and self $self")
          sender ! CountDown(n - 1)
        }
        else
          context.system.terminate()
    }

  }

  val system = ActorSystem("CommunicatingActors1")
  val actor1 = system.actorOf(Props[CountDownActor], "CountDownActor1")
  val actor2 = system.actorOf(Props[CountDownActor], "CountDownActor2")

  actor1 ! StartCounting(10, actor2)

}
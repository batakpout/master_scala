package com.aamir.akka

import akka.actor.{Actor, ActorSystem, Props}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object TestBlock {
  def someMethod = {
    Thread.sleep(7000)
    import scala.concurrent.ExecutionContext.Implicits.global
    Future(1)
  }
}
class Blocking extends Actor {
  override def receive: Receive = {
    case _: String => {
      val res: Int = Await.result(TestBlock.someMethod, Duration(8, TimeUnit.SECONDS))
      println(s"---future result--$res---")
    }
    case i: Int => println(s"---Int received---$i--")
  }
}

object BlockingTest extends App {
  val system = ActorSystem("BlockingTest")
  val actorRef = system.actorOf(Props[Blocking], "BlockingActor")

  actorRef ! "some string"
  Thread.sleep(2000)
  actorRef ! 10
  Thread.sleep(1000)
  actorRef ! 20
  Thread.sleep(20000)
}
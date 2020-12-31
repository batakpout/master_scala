package com.aamir.akka.actors

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, Promise}
import scala.concurrent.duration.DurationInt
import scala.util.Failure
import scala.util.Success

class AskTest extends Actor {
  override def receive: Receive = {
    case x: String =>
      Thread.sleep(100000)
      sender() ! "Hello"
  }
}

object AskTest extends App {
  val system = ActorSystem("AsktestAS")
  implicit val executionContext: ExecutionContext = system.dispatcher

  val actorRef = system.actorOf(Props[AskTest], "AstrtestActor")

  implicit val timeOut = Timeout(4.seconds)
  val res = actorRef ? "hello"

  val p = Promise[String]()

   res.onComplete {
     case Success(v) =>
       println(v)
       p.success("Success")
     case Failure(f) =>
       println("failure kailure")
       p.failure(throw new Exception("Failed"))
   }

  Thread.sleep(70000000)

}
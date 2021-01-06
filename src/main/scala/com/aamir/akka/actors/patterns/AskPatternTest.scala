package com.aamir.akka.actors.patterns

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

case object Test1
case object Test2
case object Test3

class AskPatternTest extends Actor {

  implicit val ex: ExecutionContext = context.system.dispatcher
  implicit val timeout: Timeout = Timeout(5.seconds)

  val childRef = context.actorOf(Props[ChildAsk], "Child")
  override def receive: Receive = {
    case x: String => (childRef ? x).map {x =>
      println("re is " + x)
    }
    case Test1 => println("from Test1")
    case Test2 => println("from Test2")
  }
}

class ChildAsk extends Actor {
  override def receive: Receive = {
    case x: String => {
      if(x == "abc")
      sender() ! Test2
      else {
        println("vavaviva")
        println("kivaaa")
      }
    }
 /*     if(x == "abc") {
        println(sender().path)
        println(context.self.path)
        println(context.parent.path)
        sender() ! Test1
      }
      println(" this will not execute if x is abc")
      if (x == "def") {
        println("xis def")
        val x = 10
        println("x is " + 10)
        sender() ! Test2
      }
      println("vavaviva")*/
  }
}

object ll extends App {
  val s = ActorSystem("asktest")
  val actorRef = s.actorOf(Props[AskPatternTest], "AskPatternTest")
  actorRef ! "abcd"
  Thread.sleep(10000)
}
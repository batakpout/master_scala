package com.aamir.advancedscala.futures

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.DurationInt

object FutureTest1 extends App {

  def someService(url: String): Future[String] = {
    Thread.sleep(9000)
    Future.successful(url)
  }

  def middleMethod(url: String) = {
    Future(someService(""))
    Future(someService(""))
    Future.successful("done")
  }

  middleMethod("").onComplete {
    case Success(s) => println(s)
    case Failure(e) => println(e.getMessage)
  }

  Thread.sleep(1000)

}

class A extends Actor {

  def someService(url: String): Future[String] = {
    Thread.sleep(9000000)
    Future.successful(url)
  }

  def middleMethod(url: String) = {
    Future(someService(""))
    Future(someService(""))
    Future.successful("done")
  }

  /*
    middleMethod("").onComplete {
      case Success(s) => println(s)
      case Failure(e) => println(e.getMessage)
    }*/

  def receive = {
    case 1 => sender() ! middleMethod("")

  }
}

class B extends Actor {
  val actorRef             = context.actorOf(Props[A], "a")
  val ex: ExecutionContext = context.system.dispatcher

  override def receive: Receive = {
    case 11 => {
      implicit val timeOut = Timeout(15.minutes)
      ask(actorRef, 1).map { res =>
        println("got it" + res)
      }
    }
  }
}

object FutureTest2 extends App {
  val system = ActorSystem("futuretest")
  val actorRef = system.actorOf(Props[B], "b")
  actorRef ! 11

}
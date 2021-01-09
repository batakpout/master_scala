package com.aamir.advancedscala.futures

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import akka.pattern.ask
import akka.pattern.pipe
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

  def someService(url: String): Future[Int] = {
    Future.failed(new Exception("11/2 kkbg"))
  }

  def middleMethod(url: String) = {
   /* Future(someService(""))
    Future(someService(""))
    Future.successful("done")*/
    val p = Promise[Int]()
    someService("10").onComplete{
      case Success(e) => {
        println("promise success")
        p.success(e)
      }
      case Failure(e) => {
        println("promise failure")
        p.failure(e)
      }
    }
    p.future
  }

  /*
    middleMethod("").onComplete {
      case Success(s) => println(s)
      case Failure(e) => println(e.getMessage)
    }*/

  def receive = {
    case 1 => {
      val res = middleMethod("").pipeTo(sender())
    }
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
      }.recover {
        case e =>
          println("fail" + e.getMessage)
      }
    }
  }
}

object FutureTest2 extends App {
  val system   = ActorSystem("futuretest")
  val actorRef = system.actorOf(Props[B], "b")
  actorRef ! 11

  Thread.sleep(10000)

}
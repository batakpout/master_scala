package com.aamir.advancedscala.futures

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object AwaitTest1 extends App {

  def divide() = {
    Future(11 / 0)
  }
   def service(url: String): Future[Int] = {
     divide()
   }

  Await.ready(service(""), Duration.Inf).onComplete {
    case Success(s) => println(s)
    case Failure(e) => println(e.getMessage)
  }

}

object AwaitTest2 extends App {

  def a = Future { Thread.sleep(2000); 100 }
  def b = Future { Thread.sleep(2000); throw new NullPointerException }

  //Await.ready(a, Duration.Inf) // Future(Success(100))
  //Await.ready(b, Duration.Inf) // Future(Failure(java.lang.NullPointerException))

  //Await.result(a, Duration.Inf) // 100
  Await.result(b, Duration.Inf)
  Thread.sleep(1000)
}
package promisesfutures

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

object Example1 extends App {
  //Futures are only completed on the end of an asynchronous computation:

  val f1: Future[List[Int]] = Future {
    // makeSomeNumbers() //when this finishes, the Future is completed.
    List(1)
  }

  f1 onSuccess {
    case foo => println("I was completed when makeSomeNumbers finished")
  }
  //Whereas Promises can produce a future that can be completed manually.

  val p = Promise[String]()
  val f = p.future

  p success ("hi") //when success is called, the Future is completed.

  f onSuccess {
    case foo => println("I was completed because a Promise told me to do so")
  }
  Thread.sleep(2000)
}
  //The callback passed to onSuccess does not complete the Future, it only listens when the Future is completed
// and does something.
//In the case of Promises, you can call its success method to complete its associated Future.
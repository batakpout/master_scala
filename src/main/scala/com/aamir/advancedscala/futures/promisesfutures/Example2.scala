package advancedscala.futures.promisesfutures

import scala.concurrent.Promise
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
object Example2 extends App {
  //You can not complete a Future.
  //A Future is supposed to be a computation and this computation (once started) completes when it does.
  // You have no control over it after creating it.
  // You can assign onComplete callback to it which will be fired when this future completes
  // but you have no control over when it will.

  //If you want to have a Future whose completion you can control, you can Promise that Future like a politician.
  // Now whether you complete that future with success or failure is up to you.

  // lets promise an Int
  val promise = Promise[Int]()

  // Now... we also have a future associated with this promise
  val future = promise.future

  // assign a callback which will be called when future completes
  future.onComplete {
    case Success(i)  => println("Future complete :: " + i)
    case Failure(ex) => println("Future failed :: " + ex.toString)
  }

  // Future by itself provides you no way to complete it

  // if you want to complete this future, you can fulfil your promise
  promise.complete(Try(10))
  Thread.sleep(2000)
}
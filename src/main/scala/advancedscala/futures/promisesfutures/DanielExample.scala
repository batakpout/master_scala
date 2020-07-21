package advancedscala.futures.promisesfutures

import java.util.Random

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object DanielExample extends App {

  def inSequence[A](fa: => Future[A], fb: => Future[A]): Future[A] = fa.flatMap(_ => fb)

  def first[A](first: Future[A], second: Future[A]): Future[A] = {
    val promise = Promise[A]
    /*

        def tryComplete[A](promise: Promise[A], result: Try[A]) = result match {
          case Success(value) => try {
            promise.success(value)
          } catch {
            case _ =>
          }
          case Failure(e)     => try {
            promise.failure(e)
          } catch {
            case _ =>
          }
        }

        first.onComplete(tryComplete(promise, _))
        second.onComplete(tryComplete(promise, _))
    */

    first.onComplete(promise.tryComplete)
    second.onComplete(promise.tryComplete)

    /*      first.onComplete {
          case Success(value) => try {
            promise.success(value)
          } catch {case _  =>}
          case Failure(e)     => try{promise.failure(e)} catch {case _ =>}
        }

        second.onComplete {
          case Success(value) => try{ promise.success(value)}catch {case _ =>}
          case Failure(e)     => try { promise.failure(e)} catch { case _ =>}
        }*/
    promise.future
  }

  val f1 = Future {
    43
  }.foreach { x =>
    println(" f1 res " + x)
  }
  val f2 = Future {
    50
  }.foreach { x =>
    println(" f2 res " + x)
  }

  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    // 2 promise which the LAST future will complete
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplete = (result: Try[A]) =>
      if (!bothPromise.tryComplete(result))
        lastPromise.complete(result)

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future
  }

  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] = {
    action().filter(condition).recoverWith {
      case _ => retryUntil(action, condition)
    }
  }

  val random = new Random()
  val action: () => Future[Int] = () => Future {
    val nextValue = random.nextInt(100)
    println("generated: " + nextValue)
    nextValue
  }

  retryUntil(action, (x: Int) => x < 5).foreach(result => println(s"settled at $result"))

  Thread.sleep(60000)
}
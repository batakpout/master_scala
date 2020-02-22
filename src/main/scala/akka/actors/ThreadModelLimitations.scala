package akka.actors

import scala.collection.immutable

object ThreadModelLimitations extends App {

  /**
   * OOP encapsulation is valid only in SINGLE THREADED MODEL
   **/

  class BankAccount(var amount: Long) {
    override def toString: String = s"Amount: $amount"

    // here race condition, locks to rescue
    def withdraw(money: Long) = amount -= money

    def deposit(money: Long) = amount += money

  }

  val account = new BankAccount(2000)

  for (_ <- 1 to 1000) {
    new Thread(() =>
      account.withdraw(1)
    ).start()
  }

  for (_ <- 1 to 1000) {
    new Thread(() =>
      account.deposit(1)
    ).start()
  }

  //Thread.sleep(5000)
  println(account.amount)

  //oops encapsulation is broken in multithreading environment
  //synchronization, locks to the rescue
  //leads to deadlocks, livelocks
}

object ThreadModelLimitations2 extends App {
  /**
   * Delegating something to a thread is Pain in the head
   */
  var task: Runnable = null
  val runningThread: Thread = new Thread(() =>
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for a task....")
          runningThread.wait()
        }
      }
      task.synchronized {
        println("[background] I've a task....")
        task.run()
        task = null
      }
    }
  )

  def delegateToBackgroundThread(r: Runnable) = {
    if (task == null) task = r
    runningThread.synchronized {
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(5000)
  delegateToBackgroundThread(() => println("some task to thread"))
  Thread.sleep(5000)
  delegateToBackgroundThread(() => println("some more to thread"))
}

object ThreadModelLimitations3 extends App {
  /**
   * Tracing and dealing with error is a pain in the neck
   */

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future


  val futures: immutable.Seq[Future[Int]] = (0 to 9)
    .map(i => i * 100000 until (i * 100000) + 99999)
    .map(range => Future {
      if(range.contains(545344)) throw new RuntimeException("invalid number")
      range.sum
    })

  val sumFuture = Future.reduceLeft(futures)(_ + _)

  sumFuture.onComplete(println)


  Thread.sleep(1000)
}

/**
 *
 * */

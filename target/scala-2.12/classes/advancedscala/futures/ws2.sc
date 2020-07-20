// promises

import scala.concurrent.Promise
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global

val promise = Promise[Int]() // "controller" over a future
val future = promise.future

// thread 1 - "consumer"
future.onComplete {
  case Success(r) => println("[consumer] I've received " + r)
}

// thread 2 - "producer"
val producer = new Thread(() => {
  println("[producer] crunching numbers...")
  Thread.sleep(500)
  // "fulfilling" the promise
  promise.success(42)
  println("[producer] done")
})

producer.start()
Thread.sleep(1000)

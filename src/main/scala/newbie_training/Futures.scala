package newbie_training

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object F1 extends App {

  val f = Future {
    // some long computation
    Thread.sleep(2000)
    1 + 1
  }

  val r: Int = Await.result(f, 1.seconds)
  println(r)

  Thread.sleep(3000)
  println("other work")
}


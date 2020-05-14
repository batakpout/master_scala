package promisesfutures
import scala.util.{Success, Failure}
import scala.concurrent.Future
import scala.concurrent.Promise
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.ThreadLocalRandom

object Example4 extends App {


  val p = Promise[Int]()
  val f = p.future

  val producer = Future {
    val r = ThreadLocalRandom.current.nextInt(0, 2000)
    println("Inside producer: r = " + r)
    if (r < 1000)
      p success r
    else
      p failure (new Exception("r > 999!"))
  }

  val consumer = Future {
    println("Inside consumer!")
    f onComplete {
      case Success(r) => println("Success: r = " + r)
      case Failure(e) => println("Error: " + e)
    }
  }

  Thread.sleep(10000)
}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
val f = Future(1)

val ff = f.filter(_ == 100).recover {
  case e => println("keeeel" + e)
}
Thread.sleep(10000)

ff.onComplete{
  case Success(s) => println("success : " + s)
  case Failure(e) => println("Failure: " + e.getMessage)
}


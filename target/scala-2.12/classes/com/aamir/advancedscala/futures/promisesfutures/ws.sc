import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
val f = Future{
  ""
}
val p = Promise[Int]
//val f = p.future

p success(10)
p success(10)


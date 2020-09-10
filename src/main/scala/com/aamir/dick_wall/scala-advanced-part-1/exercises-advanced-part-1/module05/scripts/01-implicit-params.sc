case class RetryParams(times: Int)
import scala.util.control.NonFatal
def retryCall[A](fn: => A, currentTry: Int = 0)
    (retryParams: RetryParams): A = {
  try fn
  catch {
    case NonFatal(ex) if currentTry < retryParams.times =>
      retryCall(fn, currentTry + 1)(retryParams)
  }
}
def retry[A](fn: => A)(implicit retryParams: RetryParams): A =
  retryCall(fn, 0)(retryParams)


implicit val retries = RetryParams(5)


retry {
  println("trying")
  1 / 0
}

import scala.concurrent._
import ExecutionContext.Implicits.global

Future(1)
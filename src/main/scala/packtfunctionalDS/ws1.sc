val l = List(Some(1), None, Some(3), None)
l.flatten

import scala.annotation.tailrec

@tailrec
def factorial(n: Int, acc: Int):Int = n match {
  case 1 => acc
  case _ => factorial(n - 1, n * acc)
}
factorial(5, 1)


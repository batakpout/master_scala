val l = List(Some(1), None, Some(3), None)
l.flatten

import scala.annotation.tailrec

@tailrec
def factorial(n: Int, acc: Int):Int = n match {
  case 1 => acc
  case _ => factorial(n - 1, n * acc)
}
factorial(5, 1)

val lazyView = (1 to 12).toList.view //we created a lazy collection

def isDivisibleBy(d: Int)(n: Int): Boolean = {
  println(s"checking $n divided by $d")
  n % d == 0
}

val by2 = isDivisibleBy(2) _
val by3 = isDivisibleBy(3) _
val by4 = isDivisibleBy(4) _

by2(2)
by3(3)
by4(4)

val r = lazyView filter by2
//filter by3 filter by4

lazyView.take(12).filter(by4).filter(by3).filter(by2).force



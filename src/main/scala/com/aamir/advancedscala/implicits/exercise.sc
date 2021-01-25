object Implicits {

  implicit class IntExtra(val i: Int) extends AnyVal {
    def isEven: Boolean = i % (2 : Int) == 0
    def increaseByN(n: Int = 1): Int = i + n
  }

  implicit class ListExtra(list: List[Int]) {
    def everyNMap(n: Int)(f: Int => Int): List[Int] = {
      list.zipWithIndex.map {
        case (element, i) if i % n == 0 => f(element)
        case (element, _)               => element
      }
    }
  }
}

import Implicits._

val listEnd = 15
val input = (0 to listEnd).toList
val output1= input.filter(_.isEven)
val f = (x: Int) => x.increaseByN()
val output = output1.everyNMap(2)(f)

val result = output.sum

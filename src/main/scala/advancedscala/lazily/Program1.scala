package advancedscala.lazily

import scala.collection.generic.FilterMonadic

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x)


  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n // only evaluated once
    //n + n + n + 1
    t + t + t + 1
  }

  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))


  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  // val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  //val gt20 = lt30.filter(greaterThan20)
  // println(gt20)


  /*  val lt30lazy: FilterMonadic[Int, List[Int]] = numbers.withFilter(lessThan30) // lazy vals under the hood
    val gt20lazy: FilterMonadic[Int, List[Int]] = lt30lazy.withFilter(greaterThan20)
    println
    gt20lazy.foreach(println)*/

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals!
  } yield a + 1

  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

}

object ll extends App {

  val z: List[Int] = Stream.from(1).filter(_ < 10).take(10).toList
  println(z)
}
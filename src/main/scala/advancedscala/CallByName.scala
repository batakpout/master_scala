package advancedscala

import scala.concurrent.Future
import scala.util.Try

object CallByName1 extends App {

  def byValueFunction(x: Int) = 34

  byNameFunction(2 + 3)

  def byNameFunction(x: => Int) = 90

  byNameFunction(2 + 3)

  abstract class MyList[+A] {
    def head: A

    def tail: MyList[A]
  }

  class NonEmptyList[+A](h: => A, t: => MyList[A]) extends MyList[A] {
    lazy val head: A = h
    lazy val tail: MyList[A] = t
  }

  // hold the door
  val anAttempt = Try(throw new NullPointerException)
    import scala.concurrent.ExecutionContext.Implicits.global
  val f = Future {
    //hard computation
  }

}
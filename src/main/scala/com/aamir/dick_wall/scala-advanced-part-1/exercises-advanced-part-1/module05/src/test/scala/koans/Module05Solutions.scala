
/* Copyright (C) 2010-2017 Escalate Software, LLC. All rights reserved. */

package koans
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{FunSuite, Matchers, SeveredStackTraces}
import support.StopOnFirstFailure

import scala.language.reflectiveCalls
import scala.reflect.ClassTag
import scala.util.control.NonFatal

class Module05Solutions extends FunSuite with Matchers with StopOnFirstFailure with SeveredStackTraces {

  // Look at, and understand, the Reversable[T] trait below that defines a contract
  // reverse(x: T): T that can be extended by other type classes

  trait Reversable[T] {
    def reverse(x: T): T
  }

  def reverse[T: Reversable](item: T): T = {
    implicitly[Reversable[T]].reverse(item)
  }

  // now, define an object ReversableString as an implementation of Reversable for String,
  // that simply calls reverse on the string.
  // Make it implicit

  implicit object ReversableString extends Reversable[String] {
    def reverse(x: String): String = x.reverse
  }

  // uncomment the following test to make sure it works

  test("ReversableString reverses a given string") {
    reverse("hello") should be ("olleh")
    reverse("") should be ("")
  }

  // next define an object ReversableInt which does the same thing for a Reversable[Int], by reversing the digits
  // but still returning an Int, e.g. 1458 becomes 8541
  implicit object ReversableInt extends Reversable[Int] {
    def reverse(x: Int): Int = x.toString.reverse.toInt
  }

  test("ReversableInt reverses the digits in an Int") {
    reverse(12345) should be (54321)
    reverse(1) should be (1)
    reverse(100) should be (1) // this is why this is a bit of a crap example :-)
  }

  // Write an implicit def to compose any T with reversable into a reverser for a List of T
  // that reverses both the contents of the List, and the List itself. Uncomment below to test.

  // uses the new SAMs in scala 2.12!
  implicit def reversableList[T: Reversable]: Reversable[List[T]] = { (xs: List[T]) =>
    val innerReverser = implicitly[Reversable[T]]
    xs.map(innerReverser.reverse).reverse
  }

  test("Reverse a List of Ints") {
    reverse(List(123, 456, 100)) should be (List(1, 654, 321))
  }

  test("Reverse a List of Strings") {
    reverse(List("hello", "world")) should be (List("dlrow", "olleh"))
  }


  test("Create your own intercept method") {
    // using an implicit class tag, create a new interceptException[T] method such that the following tests pass.

    // note that we use the name interceptException to avoid clashing with intercept which is a scalatest method
    // that does the same thing :-)
    // If the exception is thrown in the function, ignore it as expected, if it is not, fail with a message
    // that the exception was not thrown. If another exception than the one expected is thrown, fail with a
    // suitable method as well.

    def interceptException[T](fn: => Unit)(implicit ct: ClassTag[T]) {
      try {
        fn
        fail("Expected exception %s but didn't get it".format(ct.runtimeClass.getName))
      }
      catch {
        case NonFatal(th) if th.getClass == ct.runtimeClass => /* this is what we want - ignore */
        case NonFatal(th) =>
          fail("Expected exception %s but got exception %s".format(ct.runtimeClass.getName, th.getClass.getName))
      }
    }

    // intercept a division by zero error
    interceptException[ArithmeticException] {
      val x = 1 / 0
    }

    // now let's expect an exception where there is none. Note we use our interceptException to test our
    // interceptException - how very meta :-)
    interceptException[TestFailedException] { // we should get a test failed exception here
      interceptException[ArithmeticException] { // this is the one that should not actually occur
        val x = 1 / 1   // well now, we shouldn't get an exception here should we?
      }
    }

    // and finally test that our interceptException detects the wrong kind of exception
    interceptException[TestFailedException] {
      interceptException[NumberFormatException] {
        val x = 1 / 0   // this is an ArithmeticException, not a NumberFormatException
      }
    }
  }


  test("Implicit parameter example") {

    // In the object below: Eventually, which has a couple of case classes: MaxTries and Interval.
    // update the eventually method (along with supporting method if necessary) which takes a by-name function,
    // and add implicit values for maxTries and interval. It should try the function up to MaxTries times
    // or until the function succeeds. If the function fails on a particular try, and throws an
    // exception (the expected failure mode), it should wait for the given interval and then try again
    // assuming the maxTries has not been reached.
    // Also add the implicit values for maxTries and interval with values of 10 and 100 respectively so that
    // they are picked up by the function and used as the defaults.
    // If maxTries is reached, re-throw the exception that was thrown by the function (i.e. we give
    // up after maxTries).
    // Check that the unit tests pass.
    object Eventually {

      case class MaxTries(value: Int)
      case class Interval(value: Int)

      implicit val maxTriesForEvaluating: MaxTries = MaxTries(10)
      implicit val intervalForEvaluating: Interval = Interval(100)

      def eventually(f: => Unit)(implicit maxTries: MaxTries, interval: Interval) {
        eventuallyWith(maxTries.value, interval.value)(f)
      }

      def eventuallyWith(maxTries: Int, interval: Int)(f: => Unit): Unit = {
        var failedCount = 0
        var succeeded = false
        var optEx: Option[Throwable] = None
        while (!succeeded && failedCount < maxTries) {
          optEx =
            try {
              f
              None
            }
            catch {
              case e: Exception => Some(e)
            }
          succeeded = optEx.isEmpty
          if (!succeeded) {
            failedCount += 1
            Thread.sleep(interval)
          }
        }
        if (optEx.isDefined)
          throw optEx.get
      }
    }

    import Eventually._

    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    val it = list.iterator
    eventually { it.next should be (6) }
  }
}

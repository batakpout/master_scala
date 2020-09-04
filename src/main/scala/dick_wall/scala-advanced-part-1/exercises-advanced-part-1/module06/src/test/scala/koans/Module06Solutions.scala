
/* Copyright (C) 2010-2017 Escalate Software, LLC. All rights reserved. */

package koans

import koans.support.StopOnFirstFailure
import org.scalatest.{FunSuite, Matchers, SeveredStackTraces}

import scala.language.implicitConversions

class Module06Solutions extends FunSuite with Matchers with StopOnFirstFailure with SeveredStackTraces {

  trait Reversable[T] {
    def reverse(x: T): T
  }

  implicit object ReversableString extends Reversable[String] {
    def reverse(x: String): String = x.reverse
  }

  implicit object ReversableInt extends Reversable[Int] {
    def reverse(x: Int): Int = x.toString.reverse.toInt
  }

  implicit def reversableList[T: Reversable]: Reversable[List[T]] = { xs =>
    val tReverse = implicitly[Reversable[T]]
    xs.map(tReverse.reverse).reverse
  }


  // Write an implicit class ReverseAll to provide an extension method reverseIt to any
  // of Reversable T (using implicit bounds) so that when the reverse extension method is called
  // it reverses item
  // Note: normally you could make this extend AnyVal, but because it's defined inside another class, you
  // can't do that here. If you want to try, move the ReverseAll class, and all the Reversable
  // trait and implicit objects to the bottom of the file outside
  // of any other class, and extend AnyVal there

  implicit class ReverseAll[T: Reversable](x: T) {
    def reverseIt: T = {
      val theReverser = implicitly[Reversable[T]]
      theReverser.reverse(x)  // if you look at the solution, take a moment to grok this
    }
  }

  test("reverseAll on a Sequence of Reversables should reverse the whole seq, and each item inside") {
    List(123, 456, 789).reverseIt should be (List(987, 654, 321))
    List("Hello", "Old", "Bean").reverseIt should be (List("naeB", "dlO", "olleH"))
  }

  // meanwhile, this should not compile, make sure to understand why by uncommenting and pondering the error
  test("reverse on a sequence of not-Reversables should give a compile error") {
    assertDoesNotCompile {
      "List(2.0, 4.0, 5.0).reverseIt"
    }
  }

  // Complex numbers with implicits next

  class Complex(val r: Double, val i: Double) {
    def this(r: Double) = this(r, 0.0)

    override def toString = "" + r + " + " + i + "i"

    override def equals(other: Any) = {
      other match {
        case c: Complex => (r == c.r) && (i == c.i)
        case _ => false
      }
    }

    def +(other: Complex) = new Complex(r + other.r, i + other.i)
    def -(other: Complex) = new Complex(r - other.r, i - other.i)

    override def hashCode() = (this.r * this.i).toInt
  }

  test("Complex number addition and subtraction") {
    val n1 = new Complex(5.0, 2.0)
    val n2 = new Complex(6.0, -1.0)

    n1 + n2 should be (new Complex(11.0, 1.0))
    n2 + n1 should be (new Complex(11.0, 1.0))

    n1 - n2 should be (new Complex(-1.0, 3.0))
    n2 - n1 should be (new Complex(1.0, -3.0))

    // OK - the above lines work - no surprise there, but can you get the following lines to compile
    // after you uncomment them, using implicit definitions?

    implicit def intToComplex(i: Int): Complex = new Complex(i.toDouble, 0.0)
    implicit def doubleToComplex(d: Double): Complex = new Complex(d, 0.0)

    n1 + 1 should be (new Complex(6.0, 2.0))
    n2 - 3.0 should be (new Complex(3.0, -1.0))

    5.5 + n1 should be (new Complex(10.5, 2.0))
    -3 + n2 should be (new Complex(3.0, -1.0))
  }

  // Returning to our list of cars from earlier, implement an implicit Ordering for car so that
  // the test compiles and the cars are sorted correctly

  case class Car(name: String, year: Int, engineSizeCCs: Int)


  implicit object CarOrdering extends Ordering[Car] {
    override def compare(x: Car, y: Car): Int = {
      val yearDiff = x.year - y.year
      if (yearDiff != 0) yearDiff else x.engineSizeCCs - y.engineSizeCCs
    }

  }

  test ("Compile and sort cars correctly") {
    val car1 = Car("Grood", 1965, 1800)
    val car2 = Car("Frood", 1965, 2000)
    val car3 = Car("Shrood", 1965, 1500)
    val car4 = Car("Breg", 1963, 1000)
    val car5 = Car("Dreg", 1967, 2200)

    val cars = Vector(car1, car2, car3, car4, car5)

    cars.sorted should be (Vector(car4, car3, car1, car2, car5))
  }
}

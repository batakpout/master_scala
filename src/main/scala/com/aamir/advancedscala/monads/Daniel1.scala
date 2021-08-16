/*
package com.aamir.advancedscala.monads



object Monad1  {

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] //bind
  }

  object Attempt {
    def unit[A](a: => A): Attempt[A] = {
      try {
        Success(a)
      } catch {
        case e => Fail(e)
      }
    } //pure or apply
  }

  case class Success[A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] = {
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
    }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

}

object Monad1Test extends App {
  import Monad1._

  val o1: Attempt[Int] = Success[Int](10)
  val f: Int => Attempt[Int] = (x: Int) => unit(x)
  val r: Attempt[Int] = o1.flatMap(f)
}

//lazy monad abstracts away a computation which will only be executed when its needed.
//monad = unit + flatMap
//monad = unit + map + flatten

object Monad2 extends App {

  class Lazy[+A](value: => A) {
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=>A) => Lazy[B]):Lazy[B] = f(value)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy[A](value)
  }

  val lazyInstance: Lazy[Int] = Lazy {
    println("I am so lazy to do anything....")
    42
  }

  val flatMapInstance:Lazy[Int] = lazyInstance.flatMap { x => Lazy {
    10 * x
  }}
  println(flatMapInstance.use)
  println(flatMapInstance.use)

  /*
    left-identity
    unit(v).flatMap(f) = f(v)
    Lazy(v).flatMap(f) = f(v)
    right-identity
    l.flatMap(x => unit(x)) = l
    Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)
    associativity: l.flatMap(f).flatMap(g) = l.flatMap(x => f(x).flatMap(g))
    Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
    Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */

  // 2: map and flatten in terms  of flatMap
  /*
    Monad[T] { // List
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
      def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
      def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)
      List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
      List(List(1, 2), List(3, 4)).flatten = List(List(1, 2), List(3, 4)).flatMap(x => x) = List(1,2,3,4)
    }
   */

}*/

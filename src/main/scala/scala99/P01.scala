package scala99

import java.util.NoSuchElementException

import scala.annotation.tailrec

object P01 extends App {

  //Predefined function
  def f0[T](list: List[T]): T = list.last

  //Tail recursion
  @tailrec
  def f1[T](list: List[T]): T = list match {
    case h :: Nil  => h
    case _ :: tail => f1(tail)
    case _         => throw new NoSuchElementException("list.empty")
  }

  //Chained calls
  def f2[T](list: List[T]): T = list.reverse.head

  def f3[T]() = ((_: List[T]).head).compose((_: List[T]).reverse)




  def compose[A, T1, R](f: T1 => R, g: A => T1): A => R = x => f(g(x))

}
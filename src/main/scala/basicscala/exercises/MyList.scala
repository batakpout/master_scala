
package basicscala.exercises

import basicscala.oops.Generics.MyList

abstract class MyList[+A] {
  def head: A

  def tail: MyList[A]

  def isEmpty: Boolean

  def add[B >: A](element: B): MyList[B]

  def printElements: String

  override def toString: String = "[" + printElements + "]"

  def filter(f: A => Boolean): MyList[A]

  def map[B](f: A => B): MyList[B]

  def flatMap[B](f: A => MyList[B]): MyList[B]

  def ++[B >: A](list: MyList[B]): MyList[B]

  def sort(compare: (A, A) => Int): MyList[A]
}

trait MyPredicate[-T] {
  def test(elem: T): Boolean
}

trait MyTransformer[-A, +B] {
  def transform(elem: A): B
}


case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
  def head: A = h

  def tail: MyList[A] = t

  def isEmpty: Boolean = false

  def add[B >: A](element: B): MyList[B] = new Cons[B](element, this)

  def printElements: String = {
    if (t.isEmpty) h + "" else h + " " + t.printElements
  }

  def filter(f: A => Boolean): MyList[A] = {
    if (f(h)) Cons(h, t.filter(f)) else t.filter(f)
  }

  def map[B](f: A => B): MyList[B] = {
    Cons(f(h), t.map(f))
  }


  def ++[B >: A](list: MyList[B]): MyList[B] = {
    Cons(h, t ++ (list))
  }

  def flatMap[B](f: A => MyList[B]): MyList[B] = {
    f(h) ++ t.flatMap(f)
  }

  def sort(compare: (A, A) => Int): MyList[A] = {
    def insert(h: A, sortedTail: MyList[A]): MyList[A] = {
      if(sortedTail.isEmpty) Cons(h, Empty)
      else if(compare(h, sortedTail.head) <= 0) Cons(h, sortedTail)
      else Cons(sortedTail.head, insert(h, sortedTail.tail))
    }
    insert(h, t.sort(compare))
  }
}

case object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: Nothing = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def printElements: String = ""

  def add[B >: Nothing](element: B): MyList[B] = Cons(element, Empty)

  def filter(f: Nothing => Boolean): MyList[Nothing] = Empty

  def map[B](f: Nothing =>  B): MyList[B] = Empty

  def flatMap[B](f: Nothing => MyList[B]): MyList[B] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

  def sort(compare: (Nothing, Nothing) => Int): MyList[Nothing] = Empty

}


object TestingMyList extends App {
  val myList: Cons[Int] = Cons[Int](1, Cons(2, Cons(3, Cons(4, Empty))))

  val tranformer1: Int => Int = _ + 2
  val predicate1: Int => Boolean = _ % 2 == 0
  val transformer2: Int => MyList[Int] = (elem: Int) => Cons(elem * elem, Empty)


  val result1 = myList.map(tranformer1)
  val result2 = myList.filter(predicate1)
  val result3 = myList ++ myList
  val result4 = myList.flatMap(transformer2)
  val result5 = Cons[Int](-2, Cons(7, Cons(1, Cons(5, Empty)))).sort((x:Int, y: Int) => x-y)

  println(result1)
  println(result2)
  println(result3)
  println(result4)
  println(result5)
}

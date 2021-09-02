package com.aamir.basicscala.exercises

abstract class MyList[+A] {
  def head: A

  def tail: MyList[A]

  def isEmpty: Boolean

  //0(1)
  def prepend[B >: A](element: B): MyList[B]

  //0(n)
  def append[B >: A](item: B): MyList[B]

  def printElements: String

  override def toString: String = "[" + printElements + "]"

  def filter(f: A => Boolean): MyList[A]

  def map[B](f: A => B): MyList[B]

  def flatMap[B](f: A => MyList[B]): MyList[B]

  def ++[B >: A](list: MyList[B]): MyList[B]

  def sort(compare: (A, A) => Int): MyList[A]

  def forEach(f: A => Unit): Unit

  def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C]

  def fold[B](start: B)(f: (B, A) => B): B
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

  //Cons(1, Cons(2, Cons(3, Cons(4, Nil))))
  override def append[B >: A](item: B): MyList[B] = Cons(head, tail.append(item))

  def prepend[B >: A](element: B): MyList[B] = new Cons[B](element, this)

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
      if (sortedTail.isEmpty) Cons(h, Empty)
      else if (compare(h, sortedTail.head) <= 0) Cons(h, sortedTail)
      else Cons(sortedTail.head, insert(h, sortedTail.tail))
    }

    insert(h, t.sort(compare))
  }

  def forEach(f: A => Unit): Unit = {
    f(h)
    tail.forEach(f)
  }

  //todo: handle unequal sizes

  def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C] = {
    if (list.isEmpty) throw new RuntimeException("Lists do not have the same length")
    Cons(zip(head, list.head), tail.zipWith(list.tail, zip))
  }

  def fold[B](start: B)(f: (B, A) => B): B = {
    t.fold(f(start, head))(f)
  }

/*  def foldLeft[B](z: B)(op: (B, A) => B): B = {
    var result = z
    this foreach (x => result = op(result, x))
    result
  }*/


  def take(n: Int): MyList[A] = {
    def loop(oldList:MyList[A], newList: MyList[A], index: Int): MyList[A] = {
      if (index == 0) newList
      else loop(oldList.tail, newList.prepend(head), index - 1)
    }

    loop(tail.append(head), Empty, n)
  }

  val res= List(1,2).flatMap(y => List(3, 4).map{ x => x + y})


}

case object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: Nothing = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def printElements: String = ""

  def prepend[B >: Nothing](element: B): MyList[B] = Cons(element, Empty)

  def append[B >: Nothing](element: B): MyList[B] = Cons(element, Empty)

  def filter(f: Nothing => Boolean): MyList[Nothing] = Empty

  def map[B](f: Nothing => B): MyList[B] = Empty

  def flatMap[B](f: Nothing => MyList[B]): MyList[B] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

  def sort(compare: (Nothing, Nothing) => Int): MyList[Nothing] = Empty

  def forEach(f: Nothing => Unit): Unit = ()

  def zipWith[B, C](myList: MyList[B], zip: (Nothing, B) => C): MyList[C] = {
    if (!myList.isEmpty) throw new RuntimeException("Lists do not have the same length")
    Empty
  }

  def fold[B](start: B)(f: (B, Nothing) => B): B = start
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
  val result5 = Cons[Int](-2, Cons(7, Cons(1, Cons(5, Empty)))).sort((x: Int, y: Int) => x - y)

  println(result1)
  println(result2)
  println(result3)
  println(result4)
  println(result5)

  myList.forEach((x: Int) => print(x + ","))

  println()
  val zipList1 = Cons[String]("Hello", Cons("Bro", Empty))
  val zipList2 = Cons[Int](1, Cons(2, Empty))

  val result6: MyList[String] = zipList2.zipWith(zipList1, (x: Int, y: String) => x + "--" + y)
  println(result6)

  val result7 = myList.fold(0)(_ + _)
  println(result7)

  // for comprehensions

  val listOfIntegers: Cons[Int] = Cons[Int](1, Cons(2, Cons(3, Cons(4, Empty))))
  println(s"take test: ${listOfIntegers.take(2)}")
  val listOfStrings = Cons[String]("Hello", Cons("Mellow", Cons("Hey", Cons("Jimmy", Empty))))

  val result8: MyList[MyList[String]] = listOfIntegers.map[MyList[String]] { x => listOfStrings.map[String] { s => x + "-" + s } }
  val result9 = listOfIntegers.flatMap[String] { x => listOfStrings.map[String] { s => x + "-" + s } }

  println(result8)
  println(result9)
  println("*" * 30)
  val combinations = for {
    n <- listOfIntegers
    string <- listOfStrings
  } yield n + "-" + string
  println(combinations)
}
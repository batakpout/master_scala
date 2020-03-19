
package basicscala.exercises

import basicscala.oops.Generics.MyList

abstract class MyList[+A] {
  def head: A

  def tail: MyList[A]

  def isEmpty: Boolean

  def add[B >: A](element: B): MyList[B]

  def printElements: String

  override def toString: String = "[" + printElements + "]"

  def filter(predicate: MyPredicate[A]): MyList[A]

  def map[B](transformer: MyTransformer[A, B]): MyList[B]

  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B]

  def ++[B >: A](list: MyList[B]): MyList[B]
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

  def add[B >: A](element: B): MyList[B] = new Cons[B](h, this)

  def printElements: String = {
    if (t.isEmpty) h + "" else h + " " + t.printElements
  }

  def filter(predicate: MyPredicate[A]): MyList[A] = {
    if (predicate.test(h)) Cons(h, t.filter(predicate)) else t.filter(predicate)
  }

  def map[B](transformer: MyTransformer[A, B]): MyList[B] = {
    Cons(transformer.transform(h), t.map(transformer))
  }


  def ++[B >: A](list: MyList[B]): MyList[B] = {
    Cons(h, t ++ (list))
  }

  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B] = {
    transformer.transform(h) ++ t.flatMap(transformer)
  }

}

case object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: Nothing = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def printElements: String = ""

  def add[B >: Nothing](element: B): MyList[B] = Cons(element, Empty)

  def filter(predicate: MyPredicate[Nothing]): MyList[Nothing] = Empty

  def map[B](transformer: MyTransformer[Nothing, B]): MyList[B] = Empty

  def flatMap[B](transformer: MyTransformer[Nothing, MyList[B]]): MyList[B] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list
}


object TestingMyList extends App {
  val myList: Cons[Int] = Cons[Int](1, Cons(2, Cons(3, Cons(4, Empty))))

  val tranformer1 = new MyTransformer[Int, Int] {
    override def transform(elem: Int): Int = elem + 2
  }
  val predicate1 = new MyPredicate[Int] {
    override def test(elem: Int): Boolean = elem % 2 == 0
  }
  val transformer2 = new MyTransformer[Int, MyList[Int]] {
    override def transform(elem: Int): MyList[Int] = {
      Cons(elem * elem, Empty)
    }
  }

  val result1 = myList.map(tranformer1)
  val result2 = myList.filter(predicate1)
  val result3 = myList ++ myList
  val result4 = myList.flatMap(transformer2)

  println(result1)
  println(result2)
  println(result3)
  println(result4)
}

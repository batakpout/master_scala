package advancedscala

import basicscala.oops.Generics.MyList

object AdvancedPatternMatching1 extends App {

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(arg: Person): Option[(String, Int)] = {
      Some((arg.name, arg.age))
    }

    def unapply(age: Int): Option[String] = {
      Some(if (age < 20) "minor" else "major")
    }
  }

  val bob = new Person("bobby", 28)
  println {
    bob match {
      case Person(n, a) => s"$n is $a years old..."
    }
  }

  println {
    bob.age match {
      case Person(a) => s"I am a $a..."
    }
  }

  object even1 {
    def unapply(x: Int): Option[Boolean] =
      if (x % 2 == 0) Some(true) else None
  }

  object singleDigit1 {
    def unapply(x: Int): Option[Boolean] =
      if (x > -10 && x < 10) Some(true) else None
  }

  //None throws MatchError
  println {
    7 match {
      case singleDigit1(n) => s"is single digit == $n"
      case even1(n)        => s"is even == $n"
    }
  }
  println("*" * 40)

  object even2 {

    def unapply(x: Int): Boolean = x % 2 == 0
  }

  object singleDigit2 {
    def unapply(x: Int): Boolean = x > -10 && x < 10
  }

  println {
    12 match {
      case singleDigit2() => s"single digit ...."
      case even2()        => s"even number......."
    }
  }

  println("--" * 40)

  //infix patterns....
  case class Or[A, B](a: A, b: B)

  val either = Or(2, "two")
  println {
    either match {
      case a Or b => s"$a is written as $b"
    }
  }

  val numbers = List(1, 2)
  numbers match {
    case List(1, 2, _*) => "list of 1,2 and many more"
  }

  abstract class MyList[+A] {
    def head: A = ???

    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Cons(4, Empty))))

  object MyList {
    def unapply[A](myList: MyList[A]): Option[MyList[A]] = Some(Empty)
  }
  println {
    myList match {
      case MyList(1, 2, _*) => s"my big list"
    }
  }
}

object PF extends App {

  object SingleDigit {
    def unapply(arg: Int): Option[Int] =
      if (0 <= arg && arg < 10) Some(arg) else None
  }

  object Even {
    def unapply(arg: Int): Option[Int] =
      if (arg % 2 == 0) Some(arg) else None
  }

  val classify: Int => String = {
    case SingleDigit(n) => s"A single digit number ($n)"
    case Even(n)        => s"An even number ($n)"
    case n              => s"Something else ($n)"
  }

  println(classify(8))
  println(classify(16))
  println(classify(25))

}
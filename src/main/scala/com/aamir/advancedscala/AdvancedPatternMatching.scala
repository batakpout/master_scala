package advancedscala

import basicscala.oops.Generics.MyList

object AdvancedPatternMatching1 extends App {

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(arg: Person): Option[(String, Int)] = {
      Some(("kalumama", 66))
    }

    def unapply(age: Int): Option[String] = {
      Some(if (age < 20) "minor" else "major")
    }
  }

  val bob = new Person("bobby", 28)
  println {
    bob match {
      case p@Person(n, a) => {
        println(s"name binding: name = ${p.name}, age = ${p.age}")
        s"$n is $a years old..."
      }
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
    def unapply(x: Int): Boolean = false //x % 2 == 0
  }

  object singleDigit2 {
    def unapply(x: Int): Boolean = true //x > -10 && x < 10
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
      //case Or(a, b) => s"$a is written as $b"
    }
  }

  val intList = List(1, 2, 3)
  println {
    intList match {
      case h :: t   => "" //infix pattern
      case ::(h, t) => ""
      //case Or(a, b) => s"$a is written as $b"
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
    //must take seq, return Option of Seq
    def unapplySeq[A](myList: MyList[A]): Option[Seq[A]] = {
      if (myList == Empty) Some(Seq()) else {
        unapplySeq(myList.tail).map(x => x :+ myList.head) //:+ appends to Seq()
      }
    }

    /* def unapply[A](myList: MyList[A]): Option[A] = {
       Some(myList.head)
     }*/
  }

  println {
    myList match {
      //, if we get only 1 item in Seq, then this pattern passes case MyList(x) => println(s"x...." + x)
      //  case MyList(x) => println(s"x...." + x)
      case MyList(4, x, _*) => println(s"x...." + x)
    }
  }

  // custom return types for unapply
  // isEmpty: Boolean, get: something.
  //https://stackoverflow.com/questions/46897540/why-i-have-to-return-some-in-unapply-method
  abstract class Wrapper[T] {
    def isEmpty: Boolean

    def get: T
  }

  object PersonWrapper {

    def unapply(arg: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false

      def get: String = arg.name
    }
  }

  println {
    bob match {
      case PersonWrapper(n) => s"name is $n"
      case _                => "An alien"
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
  val classify2: PartialFunction[Int, String] = {
    case SingleDigit(n) => s"A single digit number ($n)"
    case Even(n)        => s"An even number ($n)"
    case n              => s"Something else ($n)"
  }
  println(classify(8))
  println(classify(16))
  println(classify(25))

}
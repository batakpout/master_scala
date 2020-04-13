package advancedscala

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
      if(x % 2 == 0) Some(true) else None
  }

  object singleDigit1 {
    def unapply(x: Int): Option[Boolean] =
      if(x > -10 && x < 10) Some(true) else None
  }
//None throws MatchError
  println {
    7 match {
      case singleDigit1(n) => s"is single digit == $n"
      case even1(n) => s"is even == $n"
    }
  }
  println("*" * 40)
  object even2 {

    def unapply(x: Int):Boolean = x % 2 == 0
  }

  object singleDigit2 {
    def unapply(x: Int):Boolean = x > -10 && x < 10
  }

  println {
    12 match {
      case singleDigit2() => s"single digit ...."
      case even2() => s"even number......."
    }
  }
}
package lectures.part4pm

import basicscala.exercises.{Empty, Cons, MyList}

object AllThePatterns extends App {

    // 1 - constants
    val x: Any = "Scala"
    val constants = x match {
      case 1 => "a number"
      case "Scala" => "THE Scala"
      case true => "The Truth"
      case AllThePatterns => "A singleton object"
    }

    println(constants)
    // 2 - match anything
    // 2.1 wildcard
    val matchAnything = x match {
      case _ =>
    }

    // 2.2 variable
    val matchAVariable = x match {
      case something => s"I've found $something"
    }

  println(matchAVariable)
    // 3 - tuples
    val aTuple = (1,2)
    val r = aTuple match {
      case (1, 1) =>
      case (something, 2) => s"I've found $something"
    }

  println(r)
    val nestedTuple = (1, (2, 3))
    val matchANestedTuple = nestedTuple match {
      case (_, (2, v)) => println("matched nested tuple")
      case _ => println("nope doesn't matcj")
    }
    // PMs can be NESTED!

    // 4 - case classes - constructor pattern
    // PMs can be nested with CCs as well
    val aList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
    val matchAList = aList match {
      case Empty => println("matched empty")
      case Cons(head, Cons(subhead, subtai)) => println("matched conned... subhead==>" + subhead)
    }

    // 5 - list patterns
    val aStandardList = List(1,2,3,42)
    val standardListMatching = aStandardList match {
      case List(1, _, _, _) => // extractor - advanced
      case List(1, _*) => // list of arbitrary length - advanced
      case 1 :: List(_) => // infix pattern
      case 1 +:  List(_) => // infix pattern
      case List(1,2,3) :+ 42 => // infix pattern
    }

    // 6 - type specifiers
    val unknown: Any = 2
    val unknownMatch = unknown match {
      case list: List[Int] => // explicit type specifier
      case _ =>
    }

    // 7 - name binding
    val nameBindingMatch = aList match {
      case nonEmptyList @ Cons(_, _) => nonEmptyList// name binding => use the name later(here)
      case Cons(1, rest @ Cons(2, _)) => // name binding inside nested patterns
    }

    // 8 - multi-patterns
    //val aList: MyList[Int] = Cons(1, Cons(2, Empty))
    val multipattern = aList match {
      case Empty | Cons(1, _) => // compound pattern (multi-pattern)
    }

    // 9 - if guards
    val secondElementSpecial = aList match {
      case Cons(_, Cons(specialElement, _)) if specialElement % 2 == 0 =>
    }


  /*
    Question.
   */

  val numbers = List(1,2,3)
  val numbersMatch = numbers match {
    case listOfStrings: List[String] => "a list of strings" // becoz of type erasure...
    case listOfNumbers: List[Int] => "a list of numbers"
    case _ => ""
  }

  println(numbersMatch)
  // JVM trick question

}

object PatternTest extends App {
  case class Male(name: String)
  case class Female(name: String)
  val m = Male("somename")
  m match {
    case Male(s) => println(s"$s")
    //cte case Female(s) => println(s"$s")
    case _ => println("None")
  }
}
package advancedscala
/**
 * Check Partial Functions from scala.PartialFunction
 **/

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  //  {1,2,5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2))
  //  println(aPartialFunction(57273))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(98))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note: PF can only have ONE parameter type
   */

  /**
   * Exercises
   *
   * 1 - construct a PF instance yourself (anonymous class)
   * 2 - dumb chatbot as a PF
   */

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Once you start talking to me, there is no return, human!"
    case "call mom" => "Unable to find your phone without your credit card"
  }

  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)

}


import scala.collection.immutable

object AlvinAlexanderExample1 extends App {

  val divide = new PartialFunction[Int, Int] {
    def apply(x: Int): Int = 42 / x

    def isDefinedAt(x: Int): Boolean = x != 0
  }
  //println(divide(0)) //directly calls apply method, so java.lang.ArithmeticException: / by zero

  if (divide.isDefinedAt(0)) println(divide(0))
  if (divide.isDefinedAt(2)) println(divide(2))
  println(divide(1))
}

object AlvinAlexanderExample2 extends App {

  val divide2: PartialFunction[Int, Int] =  {
    case d: Int => 42 / d
    //def isDefinedAt(x: Int): Boolean = x != 0 //doesn't work here
  }

  //println(divide2(0))directly calls apply method, so java.lang.ArithmeticException: / by zero

  //explicit mention
  if (divide2.isDefinedAt(0)) println(divide2(0)) // still error, coz isDefinedAt not mentioned inside case, mean no guard defined
}

object AlvinAlexanderExample3 extends App {

  val divide2: PartialFunction[Int, Int] = {
    case d: Int if d != 0 => 42 / d
    //def isDefinedAt(x: Int): Boolean = x != 0 doesn't work here
  }

  println(divide2(0))//directly calls apply method, so scala.MatchError

  //explicit mention
  if (divide2.isDefinedAt(0)) println(divide2(0)) // isDefinedAt works because of guard condition

  if (divide2.isDefinedAt(12)) println(divide2(2))
}

object AlvinAlexanderExample4 extends App {
  //partial function
  val r: PartialFunction[Int, String] = {
    case x: Int if x != 0 => x + "q"
    case _                => "isDefinedAt not matched"
  }
  println(r(0))
  println(r(2))
}

object AlvinAlexanderExample5 extends App {

  // converts 1 to "one", etc., up to 5
  val convert1to5: PartialFunction[Int, String] = new PartialFunction[Int, String] {
    val nums = Array("one", "two", "three", "four", "five")

    def apply(i: Int): String = nums(i - 1)

    def isDefinedAt(i: Int): Boolean = i > 0 && i < 6
  }


  // converts 6 to "six", etc., up to 10
  val convert6to10: PartialFunction[Int, String] = new PartialFunction[Int, String] {
    val nums = Array("six", "seven", "eight", "nine", "ten")

    def apply(i: Int): String = nums(i - 6)

    def isDefinedAt(i: Int): Boolean = i > 5 && i < 11
  }

  val handle1to5 = convert1to5

  //println(handle1to5(12)) java.lang.ArrayIndexOutOfBoundsException

  if (handle1to5.isDefinedAt(12)) println(handle1to5(12))

  val handle1to10: PartialFunction[Int, String] = convert1to5 orElse convert6to10
  println(handle1to10) // prints <function1>
  // println(handle1to10(20)) java.lang.ArrayIndexOutOfBoundsException

  println(handle1to10(2))
  println(handle1to10(4))
  println(handle1to10(8))

}

object AlvinAlexanderExample6 extends App {

  val divide: PartialFunction[Int, Int] = {
    case d: Int if d != 0 => 42 / d
  }

  //List(2,3,4,0) map divide //scala.MatchError for 0 , collect to rescue

  val result: List[Int] = List(2, 3, 4, 0) collect divide
  /**
   * collect takes partial function as input
   * collect builds a new collection by applying partial function for all elements
   * of collection for which that partial function is defined
   */

  List(42, "cat") collect { case i: Int => i + 1 }
}

object AlvinAlexanderExample7 extends App {

  val isEven: PartialFunction[Int, String] = {
    case x if x % 2 == 0 => x + " is even"
  }

  val isOdd: PartialFunction[Int, String] = {
    case x if x % 2 == 1 => x + " is odd"
  }

  val sample = 1 to 5
  /**
   * the method orElse allows chaining another partial function to handle, for inputs outside the declared domain
   */
  val numbers: immutable.IndexedSeq[String] = sample map (isEven orElse isOdd)
  println(numbers)
}

object AlvinAlexanderExample8 extends App {

  val M: PartialFunction[Int, Int] = {
    case x if (x % 4) != 0 => x * 4
  }

  val N: PartialFunction[Int, String] = {
    case x if x > 0 => "M value is greater than 0"
  }
  val append = (x: Int) => x * 10
  val y: PartialFunction[Int, Int] =  M andThen append
  val z: PartialFunction[Int, String] = M andThen N

  println(y(7))
  println(z(7))
}

object CollectExample extends App {
  val list: List[Double] = List(4, 16, 25, -9)
  val res = list.map(Math.sqrt)
  println(res)

  val squareRootPartialFunction: PartialFunction[Double, Double] =  {
    case d: Double if d > 0 => Math.sqrt(d)
  }

  val resCol = list.collect(squareRootPartialFunction)
  println(resCol)
}
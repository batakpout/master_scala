package advancedscala.currying

object CurriesPAF extends App {

  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)
  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int): Int = x + 1

  List(1, 2, 3).map(x => inc(x)) // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  val simpleAddFunction: (Int, Int) => Int = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  // be creative!
  val add7: Int => Int = (x: Int) => simpleAddFunction(7, x)  // simplest
  val x: Int => Int => Int = simpleAddFunction.curried
  val add7_2: Int => Int = simpleAddFunction.curried(7)
  val add7_6: Int => Int = simpleAddFunction(7, _: Int) // works as well

  val add7_3 = curriedAddMethod(7) _  // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax

  val add7_5: Int => Int = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
  // y => simpleAddMethod(7, y)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName: String => String = concatenator("Hello, I'm ", _: String, ", how are you?") // x: String => concatenator(hello, x, howarewyou)
  println(insertName("Daniel"))

  val fillInTheBlanks: (String, String) => String = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator("Hello, ", x, y)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(curriedFormatter("%1.1f"))) // compiler does sweet eta-expansion for us


  /*
    2.  difference between
        - functions vs methods
        - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  byName(23)  // ok
  byName(method)  // ok
  byName(parenMethod())
  byName(parenMethod) // ok but beware ==> byName(parenMethod())
  //  byName(() => 42) // not ok
  byName((() => 42)()) // ok
  //  byName(parenMethod _) // not ok

  //  byFunction(45) // not ok
  //  byFunction(method) // not ok!!!!!! does not do ETA-expansion!

  byFunction(parenMethod) // compiler does ETA-expansion
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works, but warning- unnecessary

}
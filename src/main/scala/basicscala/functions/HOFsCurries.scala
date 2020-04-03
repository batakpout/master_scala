package basicscala.functions

object HOFsCurries extends App {

  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null
  // higher order function (HOF)

  // map, flatMap, filter in MyList

  // function that applies a function n times over a value x
  // nTimes(f, n, x)
  // nTimes(f, 3, x) = f(f(f(x))) = nTimes(f, 2, f(x)) = f(f(f(x)))
  // nTimes(f, n, x) = f(f(...f(x))) = nTimes(f, n-1, f(x))
  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if (n <= 0) x
    else nTimes(f, n - 1, f(x))

  val plusOne = (x: Int) => x + 1
  println(nTimes(plusOne, 10, 1))

  // ntb(f,n) = x => f(f(f...(x)))
  // increment10 = ntb(plusOne, 10) = x => plusOne(plusOne....(x))
  // val y = increment10(1)
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) =
    if (n <= 0) {
      (x: Int) => {
        println("last x" + x)

        x
      }
    }
    else (x: Int) => {
      println("xxxx" + x)
      nTimesBetter(f, n - 1)(f(x))
    }

  val plus10 = nTimesBetter(plusOne, 2)(1)

  println("-----")

  println(plus10)
  println("-------")
  // curried functions
  val superAdder: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
  val add3 = superAdder(3) // y => 3 + y
  println(add3(10))
  println(superAdder(3)(10))

  // functions with multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: (Double => String) = curriedFormatter("%4.2f")
  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")

  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))

  /*
    1.  Expand MyList
        - foreach method A => Unit
          [1,2,3].foreach(x => println(x))

        - sort function ((A, A) => Int) => MyList
          [1,2,3].sort((x, y) => y - x) => [3,2,1]

        - zipWith (list, (A, A) => B) => MyList[B]
          [1,2,3].zipWith([4,5,6], x * y) => [1 * 4, 2 * 5, 3 * 6] = [4,10,18]

        - fold(start)(function) => a value
          [1,2,3].fold(0)(x + y) = 6

    2.  toCurry(f: (Int, Int) => Int) => (Int => Int => Int)
        fromCurry(f: (Int => Int => Int)) => (Int, Int) => Int

    3.  compose(f,g) => x => f(g(x))
        andThen(f,g) => x => g(f(x))
   */

  def toCurry(f: (Int, Int) => Int): (Int => Int => Int) =
    x => y => f(x, y)

  def fromCurry(f: (Int => Int => Int)): (Int, Int) => Int =
    (x, y) => f(x)(y)

  // FunctionX
  def compose[A, B, T](f: A => B, g: T => A): T => B =
    x => f(g(x))

  def andThen[A, B, C](f: A => B, g: B => C): A => C =
    x => g(f(x))

  def superAdder2: (Int => Int => Int) = toCurry(_ + _)

  def add4 = superAdder2(4)

  println(add4(17))

  val simpleAdder = fromCurry(superAdder)
  println(simpleAdder(4, 17))

  val add2 = (x: Int) => x + 2
  val times3 = (x: Int) => x * 3

  val composed = compose(add2, times3)
  val ordered = andThen(add2, times3)

  println(composed(4))
  println(ordered(4))

  def rightThenLeft(left: Int => Int, right: Int => Int): Int => Int = {
    x => left(right(x))
  }

  val f: Int => Int = _ + 10
  val g: Int => Int = _ / 2

  val gThenf = f compose g // means f(g(x))
  val fTheng = f andThen g // means f then g
  println("fTheng: " + fTheng(2))
  println("gThenf: " + gThenf(2))

  //same output...order reversal
  val fTheng1 = g compose f // means f(g(x))
  val gThenf1 = g andThen f // means f then g
  println("fTheng1: " + fTheng1(2))
  println("gThenf1: " + gThenf1(2))
}

object HOFCurries2 extends App {
  def m(x: Int)(s: String) = (1 to x).map { _ => s }.toList

  val f1: String => List[String] = m(10)
  val f12 = m(10) _

  def g(x: Int)(s: => String) = (1 to x).map { _ => s }.toList

  val g1: (=> String) => List[String] = g(10)
  val g2 = g(10) _
}

object ComposeTest extends App {
  val f: String => String = _ + "200"
  val g: Int => String = _.toString

  val res = f.compose(g)
  println(res(100))

  val res2 = g.andThen(f)
  println(res2(100))

}
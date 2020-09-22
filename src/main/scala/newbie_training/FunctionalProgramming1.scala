package newbie_training

object FunctionalProgramming1 extends App {

  // Currying
  /**
     * Currying is a techinique by which we can convert a function which takes multiple arguments
   * into a function that takes a sinle argument.
   *
   * Partial Function
   * Partially Applied Function
   *
   * Collection , List
   */


  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val res = curriedFormatter("%4.2f")(Math.PI)
  val res2 = curriedFormatter("%4.6f")(Math.PI)




  /*
     Define a function which will Currify my function
   */

  def m1(a: Int, b: Int): Int = a + b
  //def m2(a: Int): Int => Int = (b:Int) => a + b


  def toCurry(g: (Int, Int) => Int):(Int => (Int => Int)) = {
    x => (y => g(x, y))
  }

  def fromCurry(g: Int => (Int => Int)) = {
    (x: Int, y:Int) => g(x)(y)
  }

  val currying: Int => (Int => Int) = toCurry(m1)
  val r: Int => Int = currying(10)


  val res11 = fromCurry(currying)


   def compose[A, B, T](f: A => B, g: T => A): T => B = {
     x => f(g(x))
   }

  def andThen[A, B, C](f: A => B, g: B => C):A => C = {
    x => g(f(x))
  }


  val outF: Int => Int = compose((x : Int) => (x * x), (x:Int) => x + 1)
  val outF2: Int => Int = andThen((x : Int) => (x * x), (x:Int) => x + 1)

  println(outF(2))
  println(outF2(2))

  val f1: (Int, Int, Int) => Int = (x: Int, y: Int, s: Int) => x + y + s




}
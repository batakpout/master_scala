package newbie_training



object SyntacticSugar extends App {

   def f(x: Int) = x + 10

   val res = 2.+(10)

  val res2 = 2 + 10

  class Person(age: Int) {

     def equalityCheck(p: Person) = this == p
  }

  val p1 = new Person(10)
  val p2 = new Person(20)

  val result = p1 equalityCheck p2
  println(res)

  def f(x: Int = 10, d: Double, s: String = "str", b: Option[Boolean] = None) = {

  }

  f(10)
  f(20, 1.2D, "hello")




}
object OOPS1 extends App {




  println("*" * 500)


  val counter = new Counter
  counter.inc.print
  counter.inc.inc.inc.print
  counter.inc(10).print
}




class Counter(val count: Int = 0) {
  def inc = {
    println("incrementing")
    new Counter(count + 1)
  }

  def dec = {
    println("decrementing")
    new Counter(count - 1)
  }

  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n-1)
  }

  def dec(n: Int): Counter =
    if (n <= 0) this
    else dec.dec(n-1)

  def print = println(count)

}
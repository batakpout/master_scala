package basicscala.functions

object RightAssociative extends App {
  class Person {
    def -->:(s: String) = println(s)
  }
  val bob = new Person
  "Hello, Bob" -->: bob
  bob.-->:("hello jarvis")
   //bob -->: "hello, gomes" CTE

  val five = List.fill[String](5) _
  println(five("dummy string"))

  val   s = scala.util.Random.shuffle("Arnold".toSeq).mkString(",")
  println(s)
  println(s)
 }

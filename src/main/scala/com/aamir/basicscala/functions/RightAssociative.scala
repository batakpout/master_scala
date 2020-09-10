package basicscala.functions

import scala.collection.immutable

object RightAssociative extends App {
  class Person {
    def -->:(s: String) = println(s)
  }
  val bob = new Person
  "Hello, Bob" -->: bob
  bob.-->:("hello jarvis")
   //bob -->: "hello, gomes" CTE



  def m1(s: Int) (y: String): immutable.Seq[Int] = {
    (1 to s) flatMap {y =>
      List(y)
    }
  }

//unapplied methods are only converted to functions when a function type is expected....
  val x: String => immutable.Seq[Int] = m1(4)
  println(x("aamir"))
  //val f = List.fill[String](5)
  println()
  //println(five("dummy string"))

 val five = List.fill[String](5) _
  five("dummy")


  val s = scala.util.Random.shuffle("Arnold".toSeq).mkString(",")
  println(s)
  println(s)
 }


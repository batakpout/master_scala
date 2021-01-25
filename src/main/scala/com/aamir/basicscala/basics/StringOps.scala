package basicscala.basics

import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

object StringOps extends App {

  val str: String = "Hello, I am learning Scala"

  println(str.charAt(2))
  println(str.substring(7, 11))
  println(str.split(" ").toList)
  println(str.startsWith("Hello"))
  println(str.replace(" ", "-"))
  println(str.toLowerCase())
  println(str.length)

  val aNumberString = "2"
  val aNumber = aNumberString.toInt
  println('a' +: aNumberString :+ 'z')
  println(str.reverse)
  println(str.take(2))

  // Scala-specific: String interpolators.

  // S-interpolators
  val name = "David"
  val age = 12
  val greeting = s"Hello, my name is $name and I am $age years old"
  val anotherGreeting = s"Hello, my name is $name and I will be turning ${age + 2} years old."
  println(anotherGreeting)

  // F-interpolators
  val speed = 1.229232323232f
  val myth = f"$name can eat $speed%2.2f burgers per minute"
  println(myth)
  /*
    /%2.2 means
      2 characters total, 2 decimal precision
   */

  val x = 12
  val rr: String = f"$x%9d" // checks for type correctness,  %3d expects x to be Int
  println(rr)

  // raw-interpolator
  //The raw interpolator “performs no escaping of literals within the string.”
  println(raw"This is a \n newline") // here raw ignores escape character inside raw character in string
  val escaped = "This is a \n newline"
  println(raw"$escaped")
}

object ParTest extends App {
  val result1: Int = (0 to 12 by 3).par.map { a =>
    val adder: Int = 2
    println(s"#seq> $a + $adder")
    a + adder
  }.sum
  println(result1)
}
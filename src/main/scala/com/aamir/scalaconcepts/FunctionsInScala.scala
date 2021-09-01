package com.aamir.scalaconcepts

object FunctionsInScala extends App {

  // DREAM: use functions as first class elements
  // problem: oop

  class Employee {
    def calculate(a: Int): String = a.toString
  }

  println(new Employee().calculate(10))

  trait FuncK[A, B] {
    def apply(a: A): B
  }

  val doubler: FuncK[Int, Int] = new FuncK[Int, Int] {
    override def apply(a: Int): Int = a * a
  }

  /*
   * So, here objects are called as if they were functions
   */
  println(doubler(4))

  //scala supports these function types out of the box
  val stringToIntConverterOld = new Function1[String, Int] {
    override def apply(s: String): Int = s.toInt
  }

  val stringToIntConverterNew = new ((String) => Int) {
    override def apply(s: String): Int = s.toInt
  }

  val syntacticSugarStringToIntConverter1: String => Int = (x: String) => x.toInt
  val syntacticSugarStringToIntConverter2:Function1[String, Int] = (x: String) => x.toInt

  val concatenator: (String, String) => String = (a: String, b: String) => a + b
  println(concatenator("Hello ", "Scala"))

  // Function1[Int, Function1[Int, Int]]
  val superAdder: Function1[Int, Function1[Int, Int]] = new Function1[Int, Function1[Int, Int]] {
    override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }

  val superAdderEquivalent: (Int) => ((Int) => Int) = (x: Int) => (y: Int) => x + y

  val adder3 = superAdder(3)
  println(adder3(4))
  println(superAdder(3)(4))

  //So, all scala functions are OBJECTS.

}
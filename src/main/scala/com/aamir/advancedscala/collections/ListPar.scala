package com.aamir.advancedscala.collections

object ListPar1 extends App {
  val result1: Int = (0 to 30 by 3).map { a =>
    val adder: Int = 2
    println(s"#seq> $a + $adder")
    a + adder
  }.sum
  println(result1)

}

object ListPar2 extends App {
  val result1: Int = (0 to 12 by 3).par.map { a =>
    val adder: Int = 2
    println(s"#seq> $a + $adder")
    a + adder
  }.sum
  println(result1)
}




package com.aamir.basicscala.either

object Program1 extends App {

   val e: List[Either[String, Int]] = List(Right(1), Left("error"))
  println(e)
}
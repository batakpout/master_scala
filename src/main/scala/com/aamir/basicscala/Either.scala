package com.aamir.basicscala


object Either1 extends App {
  /**
   * What makes it different from Try is that you can choose a type other than Throwable to represent the exception.
   * Another difference is that exceptions that occur when transforming Either values are not converted into failures.
   * Since Scala 2.12, Either has map and flatMap. These methods transform the Right case only. We say that Either is “right biased”:
   */
  val x = Right(1).filterOrElse(x => x % 2 == 0, "Odd value")
  println(x)
  val y = Left("foo").map((x: Int) => x + 1)
  println(y)

  case class DBFailure(str: String)

  val xz= DBFailure(_)
}
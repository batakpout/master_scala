package com.aamir.advancedscala.typelevel.typeclasses


object Scastie1 extends App {

  trait Foo[A]
  object Foo {
    def apply[A](): Foo[A] = new Foo[A] {}
  }

  object ValidFoos {
    implicit val FooInt: Foo[Int] = Foo[Int]()
    implicit val FooString: Foo[String] = Foo[String]()
  }

  import ValidFoos._

  def isValidFoo[A: Foo](a: A): A = {
    println(s"'${a.toString}' is a valid Foo")
    a
  }

  val a: Int = isValidFoo[Int](3)
  assert(a == 3)
  val b: String = isValidFoo[String]("abc")
  assert(b == "abc")

}
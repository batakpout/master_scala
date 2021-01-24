package com.aamir.advancedscala

object PathDependentTypesInnerType extends App {

    class Outer {
      class Inner
      object InnerObject
      type InnerType

      def print(i: Inner) = println(i)
      def printGeneral(i: Outer#Inner) = println(i)
    }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    val s: HelperType = "str"
    s.toInt
  }

  val o = new Outer
  val inner = new o.Inner

  val x = new Outer
  val y = new x.Inner

   o.print(inner)
   o.printGeneral(inner)
   o.printGeneral(y)

  val oo = new Outer
  oo.printGeneral(inner)

  /*
   Exercise
   DB keyed by Int or String, but maybe others
  */

  /*
    use path-dependent types
    abstract type members and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }

  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[A <: ItemLike](key: A#Key): A = ???

  get[IntItem](10)
  get[StringItem]("hello")
  //get[StringItem](10)

}


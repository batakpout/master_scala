
package com.aamir.advancedscala.typelevel.typesystem

object E1 extends App {
  def callSpeak[A <: {def speak(): Unit}](obj: A) = {
    obj.speak()
  }

  class Dog {
    def speak() {
      println("woof")
    }
  }

  class Cat {
    def speak() {
      println("meow!")
    }
  }

  class Croc {
    def eat() {
      println("eating!")
    }
  }

  callSpeak(new Dog)
  callSpeak(new Cat)
  //callSpeak(new Croc)
  /**
   * As a word of warning (Cavet),
   * this technique uses reflection, so you may not want to use it when performance is a concern.
   */
}

object E2 extends App {
  //structural typing

  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("Yeah, I'm Closing")
  }

  type UnifiedCloseable = {
    def close(): Unit
  } // structural type

  def closeQuietly(unifiedCloseable: UnifiedCloseable) = unifiedCloseable.close()

  closeQuietly(new HipsterCloseable)
  closeQuietly(new JavaCloseable {
    override def close(): Unit = ???
  })
}

object E3 extends App {
  // type refinements
  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("Yeah, I'm Closing")
  }

  // AdvancedCloseable is  JavaCloseable with extra method, so type refinement
  type AdvancedCloseable = JavaCloseable {
    def closeQuietly(): Unit
  }

  class AdvancedJavaCloseable extends JavaCloseable {
    override def close(): Unit = ???

    def closeQuietly(): Unit = println("")
  }

  def closeShhh(advancedCloseable: AdvancedCloseable) = advancedCloseable.closeQuietly()

  closeShhh(new AdvancedJavaCloseable)
  //closeShhh(new HipsterCloseable)
}

object E4 extends App {
  //structural types as standalone types
  def altClose(closeable: {def close(): Unit}) = closeable.close()

  def altClose2[A <: {def close(): Unit}](obj: A) = obj.close()
}

object E5 extends App {

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog  {
    def makeSound(): Unit = println("bowbow")
  }

  class Cat  {
    def makeSound(): Unit = println("meow")
  }

  val dog: SoundMaker = new Dog()
  val cat: SoundMaker = new Cat()

    //static duck typing
   /** duck-test
    if something looks like a duck, swims, flies like duck, we can treat something as Duck i.e
    if an instance conforms to some structure (here makeSound) defined by some type, then I can use it as an
    instance of that type.
    */

}

object E6 extends App {

  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Brain {
    override def toString: String = "BRAINZ!"
  }

  class Human {
    def head: Brain = new Brain
  }

  def f[T](somethingWithAHead: {def head: T}): Unit = println(somethingWithAHead.head)

  case object CBNil extends CBL[Nothing] {
    def head: Nothing = ???
    def tail: CBL[Nothing] = ???
  }

  case class CBCons[T](override val head: T, override val tail:CBL[T]) extends CBL[T]
  f(CBCons(1, CBNil))
  f(new Human)

  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  val b = new Brain
  val brainzList = CBCons(new Brain, CBNil)
  val stringsList = CBCons("new Brain str", CBNil)

  println {
    HeadEqualizer.===(brainzList, stringsList)
  }

  println {
    HeadEqualizer.===(brainzList, new Human)
  }

  // problem:
  println{
    HeadEqualizer.===(new Human, stringsList)
  } // not type safe

}
package advancedscala

import java.{util => ju}
object JavaScalaConverters extends App {

  import collection.JavaConverters._
  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5 ) foreach(javaSet.add)

  val scalaSet = javaSet.asScala

  /* conversions java to mutable scala collections
   Iterator
   Iterable
   ju.List - collection.mutable.Buffer
   ju.Set - collection.mutable.Set
   ju.Map - collection.mutable.Map
  */

  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1,2,3,4,5)
  val juNumbersBuffer = numbersBuffer.asJava

  println(juNumbersBuffer.asScala eq numbersBuffer) //true

  val numbers = List(1,2,3)
  val juNumbers = numbers.asJava
  val backToScala = juNumbers.asScala
  println(backToScala eq numbers) // false
  println(backToScala == numbers) // true

/*  class ToScala[T](value: => T) {
    def asScala: T = value
  }

  implicit def asScalaOptional[T](o: ju.Optional[T]): ToScala[Option[T]] = new ToScala(
    if(o.isPresent) Some(o.get) else None
  )*/

  trait ToScala2[T] {
    def asScala:T
  }
  implicit class asScalaOptional2[T](o: ju.Optional[T]) extends ToScala2[Option[T]] {
    def asScala: Option[T] = if(o.isPresent) Some(o.get) else None
  }

  val juOptional = ju.Optional.of(2)
  println(juOptional.asScala)
}

//two ways to declare implicits
object ImplicitChoices1 extends App {
  case class Person(name: String) {
    def address = s"$name lives in India"
  }

  implicit def convert(name: String) = Person(name)

  println("aamir".address)
}

object ImplicitChoices2 extends App {

  trait Person {
    def address: String
  }

  implicit class PersonOps(name: String) extends Person {
    def address = s"$name lives in India"
  }

  println("aamir".address)
}

package com.aamir.cats.typeclasses

/**
 * The power of type classes and implicits lies in the compiler’s ability to combine implicit definitions when searching for candidate instances
 * This is sometimes known as type class composition.
 * We can actually define instances in two ways:
 * by defining concrete instances as implicit val's of the required type;
 * by defining implicit methods to construct instances from other type class instance (demonstrated here)
 */
object Program2A extends App {

  case class Person(name: String, address: String, age: Int)

  trait Json

  case class JString(s: String) extends Json

  case class JNumber(s: Double) extends Json

  case class JObject(m: Map[String, Json]) extends Json

  case object JNull extends Json

  trait JsonWriter[A] {
    def write(a: A): Json
  }

  object JsonWriter {
    //implicit values
    implicit val stringWriter = new JsonWriter[String] {
      def write(a: String): Json = JString(a)
    }
    implicit val numberWriter = new JsonWriter[Double] {
      def write(a: Double): Json = JNumber(a)
    }
    implicit val personWriter = new JsonWriter[Person] {
      def write(person: Person): Json = JObject(Map("name" -> JString(person.name),
        "address" -> JString(person.address), "age" -> JNumber(person.age)))
    }

    implicit val optionWriterString = new JsonWriter[Option[String]] {
      def write(a: Option[String]): Json = a match {
        case Some(v) => JString(v)
        case None    => JNull
      }
    }

    implicit val optionWriterNumber = new JsonWriter[Option[Double]] {
      def write(a: Option[Double]): Json = a match {
        case Some(v) => JNumber(v)
        case None    => JNull
      }
    }
  }

  object Json {
    def toJson[A](a: A)(implicit writer: JsonWriter[A]): Json = writer.write(a)
  }

  Json.toJson(Option("acc"))
  Json.toJson(Option(10.2))
  /**
     We would need a JsonWriter[Option[A]] for every A we care about in our application, headache,
     so,
     by defining implicit methods to construct instances from other type class instance (demonstrated here)
   */
}

/**
 * Recursive Implicit Resolution
 * In this way (Program2B), implicit resolution becomes a search through the space of possible combinations of implicit definitions,
 * to find a combination that creates a type class instance of the correct overall type
 */
object Program2B extends App {
  case class Person(name: String, address: String, age: Int)

  trait Json
  case class JString(s: String) extends Json
  case class JNumber(s: Double) extends Json
  case class JObject(m: Map[String, Json]) extends Json
  case object JNull extends Json

  trait JsonWriter[A] {
    def write(a: A): Json
  }

  object JsonWriter {
    //implicit values
    implicit val stringWriter = new JsonWriter[String] {
      def write(a: String): Json = JString(a)
    }
    implicit val numberWriter = new JsonWriter[Double] {
      def write(a: Double): Json = JNumber(a)
    }
    implicit val personWriter = new JsonWriter[Person] {
      def write(person: Person): Json = JObject(Map("name" -> JString(person.name),
        "address" -> JString(person.address), "age" -> JNumber(person.age)))
    }

    implicit def optionWriter[A](implicit writer: JsonWriter[A]) =
      new JsonWriter[Option[A]] {
      def write(a: Option[A]): Json = a match {
        case Some(v) => writer.write(v)
        case None    => JNull
      }
    }

  }

  object Json {
    def toJson[A](a: A)(implicit writer: JsonWriter[A]): Json = writer.write(a)
  }

  //and recursively searches for a JsonWriter[String] to use as the parameter to optionWriter
  Json.toJson(Option("acc"))
  Json.toJson(Option(10.2))
}
/**
 * implicit methods with non-implicit parameters form a different Scala pattern called an implicit conversion.
 * Implicit conversion is an older programming pattern that is frowned upon in modern Scala code.
 * Fortunately, the compiler will warn you when you do this. (Advanced language feature)
 * You have to manually enable implicit conversions by importing scala.language.implicitConversions in your file:
 */

object PrintableTest extends App {
  final case class Cat(name: String, age: Int, color: String)

  trait Printable[A] {
    def format(a: A): String
  }

  object PrintableInstances {
    implicit val intPrintable = new Printable[Int] {
      def format(a: Int): String = a.toString
    }
    implicit val stringPrintable = new Printable[String] {
      def format(a: String): String = a + "=="
    }
  }

/*  object Printable {
    def format[A](a: A)(implicit printable: Printable[A]) = printable.format(a)
    def print[A](a: A)(implicit printable: Printable[A]) = println(format(a))
  }*/

  object PrintableSyntax {

    implicit class PrintableOps[A](a: A) {
      def format1(implicit printable: Printable[A]) = printable.format(a)

      def print(implicit printable: Printable[A]) = println(format1)
    }

  }

  import PrintableSyntax._
  import PrintableInstances._

  implicit val catPrintable = new Printable[Cat] {
    def format(cat: Cat): String = {
      val name  = cat.name.format1
      val age   = cat.age.format1
      val color = cat.color.format1
      s"Name is: ${name}, Age: ${age}, COLOR: ${color}"
    }
  }

  val c = Cat("anabelle", 6, "orange")
   c.print


}
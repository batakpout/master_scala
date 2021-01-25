package com.aamir.cats.typeclasses

object Program1A extends App {

  case class Person(name: String, address: String, age: Int)

  // Define a very simple JSON AST
  trait Json
  case class JString(s: String) extends Json
  case class JNumber(s: Double) extends Json
  case class JObject(m: Map[String, Json]) extends Json
  case object JNull extends Json

  trait JsonWriter[A] {
    def write(a: A): Json
  }

  object JsonInstance {
     implicit val stringWriter = new JsonWriter[String] {
       def write(a: String): Json = JString(a)
     }
    implicit val personWriter = new JsonWriter[Person] {
      def write(person: Person): Json = JObject(Map("name" -> JString(person.name),
        "address" -> JString(person.address), "age" -> JNumber(person.age)))
    }
  }

  object Json {
    def toJson[A](a: A)(implicit writer: JsonWriter[A]):Json = writer.write(a)
  }

  import JsonInstance._

  Json.toJson("a")
  Json.toJson(Person("aamir", "Bengaluru", 32))
}

object Program1B extends App {

  case class Person(name: String, address: String)
  trait Json
  case class JString(s: String) extends Json
  case class JObject(m: Map[String, JString]) extends Json

  trait JsonWriter[A] {
    def write(a: A): Json
  }

  object JsonInstance {
    implicit val stringWriter = new JsonWriter[String] {
      def write(a: String): Json = JString(a)
    }
    implicit val personWriter = new JsonWriter[Person] {
      def write(person: Person): Json = JObject(Map("name" -> JString(person.name),
        "address" -> JString(person.address)))
    }
  }

  object JsonSyntax {
    implicit class JsonOps[A](a: A) {
      def toJson(implicit writer: JsonWriter[A]):Json = writer.write(a)
    }
  }

  import JsonInstance._
  import JsonSyntax._

  "a".toJson
  Person("aamir", "Bengaluru").toJson
}

object Program1C extends App {

  case class Person(name: String, address: String)
  trait Json
  case class JString(s: String) extends Json
  case class JNumber(i: Int) extends Json
  case class JObject(m: Map[String, JString]) extends Json

  trait JsonWriter[A] {
    def write(a: A): Json
  }

  object JsonInstance {
    implicit val stringWriter:JsonWriter[String] = new JsonWriter[String] {
      def write(a: String): Json = JString(a)
    }

    implicit val intWriter:JsonWriter[Int] = (i: Int) => JNumber(i)

    implicit val personWriter:JsonWriter[Person] = new JsonWriter[Person] {
      def write(person: Person): Json = JObject(Map("name" -> JString(person.name),
        "address" -> JString(person.address)))
    }
  }

  object Json {
    def toJson[A : JsonWriter](a: A):Json = implicitly[JsonWriter[A]].write(a)
  }

  import JsonInstance._

  Json.toJson("a")
  Json.toJson(Person("aamir", "Bengaluru"))
}
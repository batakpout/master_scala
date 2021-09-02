package com.aamir.scalaconcepts.catz


final case class Person(name: String, email: String)

//Define a very simple JSON AST
sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Number) extends Json

final case object JsNull extends Json

//The "Serialize the JSON" behavior is encoded in this trait
trait JsonWriter[A] {
  def write(value: A): Json
}

object JsonWriterInstance {

  implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
    override def write(value: String): Json = JsString(value)
  }

  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    override def write(value: Person): Json = {
      JsObject(
        Map(
          "name" -> JsString(value.name),
          "email" -> JsString(value.email)
        )
      )
    }
  }
  //Single Abstract Method
  implicit val numberWriter: JsonWriter[Int] = (value: Int) => JsNumber(value)
  //equivalent to
/*  implicit val numberWriter: JsonWriter[Int] = new JsonWriter[Int] {
    override def write(value: Int): Json = JsString(value)
  }*/
}


object Json {
  def toJson[A](value: A)(implicit writer: JsonWriter[A]): Json = {
    writer.write(value)
  }
}

object test1 extends App {
  import JsonWriterInstance.{stringWriter, personWriter}
  val result: Json = Json.toJson("Hello")
  val person: Json = Json.toJson(Person("aamir", "dropmeataaamira@gmail.com"))
  println(result)
  println(person)
}
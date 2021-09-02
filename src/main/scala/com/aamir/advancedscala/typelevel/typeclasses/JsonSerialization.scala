/*
package advancedscala.typelevel.typeclasses

import java.util.Date

import kafka.utils.json.JsonValue

object JsonSerialization extends App {
  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String = "\"" + value + "\""
  }

  final case class JSONInt(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    override def stringify = values.map { case (key, value) =>
      "\"" + key + "\" : " +  value.stringify
    }.mkString("{", ",", "}")
  }

  val jsonObject = JSONObject(
    Map(
    "user" -> JSONString("john"),
     "posts" -> JSONArray(List(
        JSONString("Scala Rocks!"),
       JSONInt(100)
     )
     ),
    "total_posts" -> JSONInt(10)
  )
  )

  //println(jsonObject.stringify)

  trait JsonConverter[T] {
    def convert(value: T): JSONValue
  }

  implicit object StringConverter extends JsonConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JsonConverter[Int] {
    override def convert(value: Int): JSONValue = JSONInt(value)
  }

  implicit object UserConverter extends JsonConverter[User] {
    override def convert(user: User): JSONValue = {
      JSONObject(
        Map(
          "name" -> user.name.toJson,
          "age" -> user.age.toJson,
          "email" -> user.email.toJson
        )
      )
    }
  }

  implicit object PostConverter extends JsonConverter[Post] {
    override def convert(post: Post): JSONValue = {
      JSONObject(
        Map(
          "content" -> post.content.toJson,
          "created_at" -> post.createdAt.toString.toJson,
        )
      )
    }
  }

  implicit object FeedConverter extends JsonConverter[Feed] {
    override def convert(feed: Feed): JSONValue = {
      JSONObject(
        Map(
          "user" -> feed.user.toJson,
          "posts" -> JSONArray(feed.posts.map(_.toJson)),
        )
      )
    }
  }

   implicit class JSONOps[T](value: T) {
     def toJson(implicit converter: JsonConverter[T]) =
       converter.convert(value)
   }

  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@rockthejvm.com")
  val feed = Feed(john, List(
    Post("hello", now),
    Post("look at this cute puppy", now)
  ))

  println(feed.toJson.stringify)
}*/

package advancedscala.typelevel.typeclasses

object Program2 extends App {

  case class User(name: String, email: String)

  //type-class
  trait HtmlSerializer[T] {
    def serialize[T](value: T): String
  }

  //type instance
  implicit object IntSerializer extends HtmlSerializer[Int] {
    override def serialize[Int](value: Int): String = s"serializing $value"
  }

  //type interface
  object Serializer {
    def serialize[T](value: T)(implicit serializer: HtmlSerializer[T]): String =
      serializer.serialize(value)
  }
  //type instance
  /*object HtmlSerializerInstances {
  implicit val intSerializer = new HtmlSerializer[Int] {
    override def serialize[T](value: T): String = s"serializing $value"
  }
}*/

     println(Serializer.serialize(10))
}
object SampleScala extends App {

  // type class
  trait Equal[T] {
    def apply(x: T, y: T): Boolean
  }

 //type-instance
  implicit object NameEquality extends Equal[User] {
    override def apply(x: User, y: User): Boolean = x.name == y.name
  }

  object FullEquality extends Equal[User] {
    override def apply(x: User, y: User): Boolean = NameEquality(x, y) && x.email == y.email
  }

  //type interface
  object Equal {
    def apply[T](a:T, b:T)(implicit equality: Equal[T]): Boolean = equality(a, b)
  }
  case class User(name: String, age: Int, email: String)

  val bob1 = User("Bob", 21, "bob@gmail.com")
  val bob2 = User("nob", 22, "nob@gmail.com")
  println(Equal[User](bob1, bob2))

}





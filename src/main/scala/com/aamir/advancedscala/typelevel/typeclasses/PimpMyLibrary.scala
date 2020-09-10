package advancedscala.typelevel.typeclasses


object PimpMyLibrary_Implicits extends App {

   implicit class RichInt(val value: Int) extends AnyVal {
     def isEven: Boolean = value / 2 == 0
     def nTimes(f: () => Unit) = {
        def rec(n: Int):Unit = {
          if(n == 0) ()
          else {
            f()
            rec(n - 1)
          }
        }
       rec(value)
     }

     def *[T](list: List[T]): List[T] = {
       def concatenate(n: Int): List[T] =
         if (n <= 0) List()
         else concatenate(n - 1) ++ list

       concatenate(value)
     }

   }

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string) // java.lang.Integer -> Int
    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  /*
      Same as
      class RichInt(value: Int) {
         def isEven: Boolean = value / 2 == 0
      }
      implicit def toRichInt(x: Int): RichInt = new RichInt(x)

      2.isEven
   */
  2.isEven
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2) // stringToInt("6") / 2

  println("3".asInt + 4)
  println("John".encrypt(2))

  3.nTimes(() => println("Scala Rocks!"))
  println(4 * List(1,2))

}

object PimpMyLibrary_TypeClasses extends App {

  case class User(name: String, email: String)

  //type-class
  trait HtmlSerializer[T] {
    def serialize(value: T): String
  }

  //type instance
  implicit object UserSerializer extends HtmlSerializer[User] {
    override def serialize(value: User): String = {
      s"${value.name} had email: ${value.email}"
    }
  }

  object HtmlSerializerInstances {
    implicit val intHtmlSerializer = new HtmlSerializer[Int] {
      override def serialize(value: Int): String = s"int serialzier"
    }

    //using single abstract method (sam)
    implicit val samIntInstance = (x: Int) => s"int serializer"
  }



  //type interface
  implicit class HtmlEnrichment[T](val value: T) extends AnyVal {
    def toHtml(implicit serializer: HtmlSerializer[T]) = serializer.serialize(value)
  }



  User("john", "johy@gmail.com").toHtml
  import HtmlSerializerInstances.intHtmlSerializer
  //AD HOC polymorphism
  //choose implementation
  2.toHtml
}

object PimpMyLibrary_TypeSafeEqual extends App {

 case class User(name: String, email: String)
  val john = User("john", "johy@gmail.com")
  val smith = User("smith", "smith@gmail.com")

  //type-class

  trait Equal[T] {
    def apply(x: T, y: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(x: User, y: User): Boolean = x.name == y.name
  }

  implicit class TypeSafeEqual[T](val value: T) extends AnyVal {
    def ===(other: T)(implicit equalizer: Equal[T]) = equalizer(value, other)
  }

  //type safe
  john === smith
  ///john === 3

}

object PimpMyLibrary_Implicitly extends App {

  case class Permission(s: String)

  implicit val defaultPermission = Permission("0896")

  val x = implicitly[Permission].s
}

object QAProgram extends App {

  trait Equal[-T] {
    def apply(x: T, y: T): Boolean
  }

  implicit class EqualEnrichment[T](value: T) {
    def ===(anotherValue: T)(implicit equal: Equal[T]): Boolean = equal(value, anotherValue)
    def !==(anotherValue: T)(implicit equal: Equal[T]): Boolean = !equal(value, anotherValue)
  }


  implicit object AnyEqual extends Equal[AnyVal] {
    override def apply(a: AnyVal, b: AnyVal): Boolean = a == b
  }

  println(1 === 1)
  /**
   * Hey Sahand,
   *
   * This is tricky. The reason why this doesn't compile is because the compiler is looking for an Equal[Int].
   * I assume you want that AnyEqual to also match an Equal[Int].
   * At the moment it doesn't, because Equal[Int] and Equal[AnyVal] are totally different types.
   * In particular, Equal[AnyVal] is not a subtype of Equal[Int], which would satisfy the compiler.
   *
   * Ring any bells?
   *
   * Yep. You need to make Equal contravariant.
   *
   * trait Equal[-T] {
   * def apply(v1: T, v2: T): Boolean
   * }
   * Daniel
   */
}
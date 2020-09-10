package advancedscala.typelevel.typeclasses

case class Cat(name: String, age: Int, color: String)

//The Type class
trait Printable[A] {
  def format(value: A): String
}

//The Type Instance
object PrintableInstance {
   //single abstract method
  implicit val stringInstance: Printable[String] = (value: String) => value.take(4)

  implicit val intInstance: Printable[Int] = new Printable[Int] {
    override def format(value: Int): String = value.toString.take(2)
  }

  implicit val catInstance = new Printable[Cat] {
    override def format(value: Cat): String =
      s"${value.color} ${value.name} is ${value.age} years old"
  }

}

//type interface
object Printable {
  def format[A](value: A)(implicit print: Printable[A]): String = print.format(value)
  def print[A](value: A)(implicit print: Printable[A]) = println(print.format(value))
  def frint[A : Printable](value: A): String = implicitly[Printable[A]].format(value)
}

//type interface
object PrintableSyntax {
  implicit class PrintOps[A](value: A) {
     def print(implicit p: Printable[A]) = p.format(value)
  }
}

object Program1Test extends App {
  import Printable._
  import PrintableInstance._
  //println(format(100).trim())
  //print("yella")
  println ( frint[String]("cbella") )

}

object Program2Test extends App {
  import PrintableSyntax._
  import PrintableInstance._
  Cat("pussy", 12, "white").print
}

object jj extends App {

  implicit class StringOps(s: String)  {
    def trimU = s.trim
  }

  "String".trimU

  new StringOps("ss").trimU
}

object mm extends App {

  object StringOpsUtil {

    implicit class StringOps(s: String) {
      def trimU = s.trim
    }

  }
  import StringOpsUtil._
  "String".trimU

}


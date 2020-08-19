package advancedscala.implicits

//defined in class, trait, objects, not as top level
object ImplicitsIntro extends App {

  val pair = "Daniel" -> "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name!"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet) // println(fromStringToPerson("Peter").greet)

  //  class A {
  //    def greet: Int = 2
  //  }
  //  implicit def fromStringToA(str: String): A = new A

  // implicit parameters
  def increment(implicit amount: Int) = 90 + amount
  implicit val defaultAmount = 10

  increment
  // NOT default args

}
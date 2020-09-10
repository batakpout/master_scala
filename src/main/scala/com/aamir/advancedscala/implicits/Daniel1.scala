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

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

  //implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)

  println(List(1, 4, 5, 3, 2).sorted)

  // scala.Predef

  /*

    Implicits (used as implicit parameters):
      - val/var
      - object
      - accessor methods = defs with no parentheses
   */

  //defined only in class, object or trait

  /*
    Implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companions of all types
   */

  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  /*object Person {
      implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
    }
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age) //precedence over object Person*/
  //println(persons.sorted)

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AgeOrdering._

  println(persons.sorted)

  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalPurchaseOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => a.nUnits < b.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  val p1 = Purchase(12, 4)
  val p2 = Purchase(22, 12)
  val p3 = Purchase(6, 333)
  val p4 = Purchase(14, 22)

  val p = List(p1, p2, p3, p4)

  println(p.sorted)

}
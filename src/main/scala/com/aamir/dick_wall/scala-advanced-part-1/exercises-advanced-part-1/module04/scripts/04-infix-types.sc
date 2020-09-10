val s = "hello"
s.charAt(1)

s charAt 1  // infix form, item, method, single parameter

case class Person(name: String)

class Loves[T1, T2] {
  def describe(i1: T1, i2: T2) = s"$i1 loves $i2"
}
case class NamedLoves(p1: String, p2: String) extends Loves[String, String] {
  def sayIt: String = describe(p1, p2)
}


case class PersonLoves(p1: Person, p2: Person) extends (Person Loves Person) {
  def sayIt: String = describe(p1, p2)
}


def sayItWithRoses(lovers: Person Loves Person) =
  s"Roses are red, violets are blue, I love you so much, I made you both stew, $lovers"

sayItWithRoses(PersonLoves(Person("Harry"), Person("Sally")))

// can't call it with NamedLoves though - wrong type:
//sayItWithRoses(NamedLoves("Fred", "Chip"))
case class Person(str: String)
case class Employee(name: String)
val p1 = Person("aamir")
val p2 = Person("aamir")
val x1@List(x, y) = List(p1, p2).map { person =>
  List(Employee("kabib"))
}
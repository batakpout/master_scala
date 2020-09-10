//https://www.journaldev.com/9609/scala-typebounds-upper-lower-and-view-bounds#what-is-type-bound-in-scala

class Person1[T <% Ordered[T]](val firstName: T, val lastName: T)

class Person[T](val firstName: T, val lastName: T)(implicit ev$1: T => Ordered[T]) {
  def greater = if (firstName > lastName) firstName else lastName
}



     val p1 = new Person("Rams","Posa")
    val p2 = new Person("Hohn","Chris")

    println(p1.greater)
    println(p2.greater)

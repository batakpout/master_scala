object Duplication extends App {
  //import scala.collection.mutable.MutableList
    def duplicateN[A](n: Int, l: List[A]):List[A] = {
      l flatMap { e => List.fill(n)(e) }
    }


    val numbers = List("h","j","u")
    println(duplicateN(3:Int,numbers))
    // List(La, La, La, La, La, La, La, La)



}

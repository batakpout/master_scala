package searching

/**
 * Worst - O(n)
 * Best - O(1)
 * Average - O(n)
 **/
object LinearSearch1 extends App {

  def linearSearch[A](list: List[A], key: A): Option[A] = list match {
    case Nil => None
    case h :: _ if h == key => Some(h)
    case _ :: tail => linearSearch(tail, key)
  }

  val result: Option[Int] = linearSearch(List(1, 2, 3, 4, 5, 6), 1)
  if (result.nonEmpty) println("Element present") else println("Not present")

}
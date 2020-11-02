import scala.collection.immutable.ListMap
object Merge extends App {
    def merge[A, B](a: Map[A, B], b: Map[A, B])(mergef: (B, Option[B]) => B): Map[A, B] = {
      val (big, small) = if (a.size > b.size) (a, b) else (b, a)
      small.foldLeft(big) { case (z, (k, v)) => z + (k -> mergef(v, z.get(k))) }
    }

    def mergeIntSum[A](a: Map[A, Int], b: Map[A, Int]): Map[A, Int] =
      merge(a, b)((v1, v2) => v2.map(_ + v1).getOrElse(v1))
    //val m1 = Map("a" -> 1, "b" -> 2)
    //val m2 = Map("b"-> 21, "a" -> 3)
    val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4)
    val m2 = Map[String, Int]("a" -> 7, "c" -> 3, "d" -> 1)
    val x = mergeIntSum(m1,m2)
    val y= ListMap(x.toSeq.sortBy(_._2):_*)
    println(y)
    // ListMap(mapIm.toSeq.sortBy(_._2):_*)

  //val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4).toList
  //val m2 = Map[String, Int]("a" -> 7, "c" -> 3, "d" -> 1).toList
}

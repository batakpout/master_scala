package Scala_Training

object Question_5 extends App{
  val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4).toList
  val m2 = Map[String, Int]("a" -> 7, "c" -> 3, "d" -> 1).toList

  val finalList = m1. ++ (m2).groupBy(_._1).map(x => (x._1, x._2.map(_._2).sum))
  println(finalList)
}

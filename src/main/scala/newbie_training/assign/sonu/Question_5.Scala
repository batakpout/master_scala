object Question_5 extends App {
  val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4).toList
  val m2 = Map[String, Int]("a" -> 7, "c" -> 3, "d" -> 1).toList

  val maps = List(m1, m2)
  val merged = maps.foldLeft(mapMonoid.zero)(mapMonoid.op)
}
package newbie_training.assign.deena

object CombineMappedLists extends App{


  val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4, "p" -> 12)
  val m2 = Map[String, Int]("a" -> 7, "b" -> 3, "d" -> 1)

  val r = m1 ++ m2
  println(r)
  val m3= m1 ++ m2.map { case (k,v) => k -> (v + m1.getOrElse(k, 0)) }
  println(m3)



}

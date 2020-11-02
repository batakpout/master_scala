//You have two map:
//val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4).toList
//val m2 = Map[String, Int]("a" -> 7, "c" -> 3, "d" -> 1).toList
//Combine them:
//output = Map(b -> 3, d -> 5, a -> 8, c -> 3)





package week1

object question_5 extends App {


    val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4)
    val m2 = Map[String, Int]("a" -> 7, "c" -> 3, "d" -> 1)
    val m3= m1 ++ m2.map{ case (k,v) => k -> (v + m1.getOrElse(k,0)) }
    println(m3)



}

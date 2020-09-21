
trait Human
case class Male(id: Int, name: String) extends Human
case class Female(id: Int, name: String) extends Human

val m1 = Male(1, "Aamir")
val m2 = Male(2, "John")
val f1 = Female(3, "Sonia")
val f2 = Female(4, "Rose")

val l: List[Human] = List(m1, m2, f1, f2)

val res = l.partition(_.isInstanceOf[Male])

res._1
res._2

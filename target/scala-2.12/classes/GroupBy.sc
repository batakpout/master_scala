val f: Int => Boolean = (x: Int) => x < 5

val l = (1 to 10).toList.groupBy(f)

val donuts: Seq[String] = Seq("Plain Donut", "Strawberry Donut", "Glazed Donut", "Pineapple Donut")

val m = collection.mutable.Map()

donuts.groupBy(_.charAt(0))


def testOptionalImplicit(implicit x: String = "") = {
  s"hi, $x"
}

implicit val x = "Hello"
testOptionalImplicit
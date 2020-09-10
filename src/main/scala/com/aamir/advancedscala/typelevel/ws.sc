//def implicitly[T](implicit e: T): T = e
implicit val i: String = "hello"
implicitly[String]
// implicitly[Int] CTE

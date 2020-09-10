trait JSONWrite[T] {
  def toJsonString(item: T): String
}

def jsonify[T: JSONWrite](item: T): String =
  implicitly[JSONWrite[T]].toJsonString(item)


implicit object StrJsonWrite extends JSONWrite[String] {
  override def toJsonString(item: String) = s""""$item""""
}

implicit def listJsonWrite[T: JSONWrite] = new JSONWrite[List[T]] {
  override def toJsonString(xs: List[T]): String = {
    val tJsonify = implicitly[JSONWrite[T]]
    xs.map(x => tJsonify.toJsonString(x)).mkString("[", ",", "]")
  }
}

jsonify("Hello")

jsonify(List("Hello", "World"))

implicit object IntJsonfify extends JSONWrite[Int] {
  override def toJsonString(item: Int) = item.toString
}

jsonify(List(1,2,3))

implicit def mapJSONWrite[T: JSONWrite] = new JSONWrite[Map[String, T]] {
  def toJsonString(m: Map[String, T]): String = {
    val pairs = for ((k, v) <- m) yield
      s"${jsonify(k)}: ${jsonify(v)}"
    pairs.mkString("{\n  ", ",\n  ", "\n}")
  }
}

jsonify(Map(
  "hello" -> List("hello", "world"),
  "goodbye" -> List("goodbye", "cruel", "world")
))

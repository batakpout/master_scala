def method1[T](list: List[T])(implicit ev: T => Ordered[T]) = {
  val r = list.head < list.last
  r
}

method1[Int](List(1,2,3))


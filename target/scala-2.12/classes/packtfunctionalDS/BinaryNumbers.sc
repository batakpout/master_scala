def carry(c: Int, list: List[Int]): List[Int] = (c, list) match {
  case (0, xs)     => xs
  case (1, Nil)    => Nil
  case (1, h :: t) => (1 - h) :: carry(h, t)
  case (_, _)      => throw new Exception("")
}
carry(1, List(1, 0, 1, 1).reverse).reverse

//https://www.youtube.com/watch?v=Ai5F0KLyh2Q
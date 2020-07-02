
def carry(c: Int, list: List[Int]): List[Int] = (c, list) match {
  case (0, xs)     => xs
  case (1, Nil)    => List(1)
  case (1, h :: t) => (1 - h) :: carry(h, t)
  case (_, _)      => throw new Exception("")
}

def add(c: Int, list1: List[Int], list2: List[Int]): List[Int] = (list1, list2) match {
  case (Nil, Nil)           => carry(c, Nil)
  case (_ :: _, Nil)        => carry(c, list1)
  case (Nil, _ :: _)        => carry(c, list2)
  case (h1 :: t1, h2 :: t2) => (c + h1 + h2) % 2 :: add((c + h1 + h2) / 2, t1, t2)
}

//println(s"add test: ${add(0, List(1, 1, 0, 0, 1).reverse, List(0, 1, 1, 0, 1).reverse).reverse}")

def mult(first: List[Int], second: List[Int]): List[Int] = {
  def multiply(ps: List[Int], qs: List[Int]): List[Int] = ps match {
    case Nil       => Nil
    case (0 :: xs) => 0 :: multiply(xs, qs)
    case (1 :: xs) => {
      val res = add(0, qs, 0 :: multiply(xs, qs))
      println("Res " + res)
      res
    }
  }

  multiply(first.reverse, second.reverse).reverse
}
//  println(s"mul test: ${mult(List(1, 0, 1, 1), List(1,1, 0, 1))}")
println(s"mul test: ${mult(List(0, 0), List(0, 1))}")

//https://www.youtube.com/watch?v=Ai5F0KLyh2Q
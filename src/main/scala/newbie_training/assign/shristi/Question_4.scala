package Scala_Training

object Question_4 extends App{
  def repeat[A](n: Int, list: List[A]): List[A]
  = list.foldLeft(List[A]())((acc, x) => acc ++ List.fill(n)(x))

  val nums: List[Int] = List(1, 2, 3, 4)
  println(repeat(3, nums))
}
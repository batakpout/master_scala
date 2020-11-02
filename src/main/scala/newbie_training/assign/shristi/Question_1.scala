package newbie_training.assign.shristi



object Question_1 extends App{
  val starttime = System.nanoTime()
  val a = Vector(1, 2, 3)
  val updatedvalue = a :+ 4
  println(updatedvalue)
  val endtime = System.nanoTime()
  println(s"Time taken to update the vector ${(endtime-starttime)} nanoseconds")

  val liststarttime = System.nanoTime()
  val x: List[Int] = List(1, 2, 3)
  val y = x:::4::Nil
  println (y)
  val listendtime = System.nanoTime()
  println(s"Time taken to update the List ${(listendtime-liststarttime )} nanoseconds")
}